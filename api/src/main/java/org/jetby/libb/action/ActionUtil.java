package org.jetby.libb.action;

import me.clip.placeholderapi.PlaceholderAPI;
import org.jetby.libb.action.record.Expression;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActionUtil {

    /**
     * Recursively schedule batches separated by [delay] entries.
     *
     * @param items            remaining items to process
     * @param batchStart       index of the first item in the current batch
     * @param accumulatedDelay total ticks elapsed so far
     */
    public static void scheduleChain(@NotNull ActionContext ctx,
                                     @NotNull List<Object> items,
                                     int batchStart,
                                     long accumulatedDelay) {

        if (ctx.getPlugin() == null) {
            throw new RuntimeException("The [delay] action was triggered, but the Plugin is null.");
        }

        if (batchStart >= items.size()) return;

        List<Object> batch = new ArrayList<>();
        long nextDelay = -1;
        int nextBatchStart = items.size();

        for (int i = batchStart; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof String line) {
                long ticks = parseDelay(line);
                if (ticks >= 0) {
                    nextDelay = ticks;
                    nextBatchStart = i + 1;
                    break;
                }
            }
            batch.add(item);
        }

        Runnable executeBatch = () -> {
            for (Object item : batch) {
                if (item instanceof String line) {
                    ActionExecute.run(ctx, line);
                } else if (item instanceof Expression expression) {
                    ActionExecute.run(ctx, expression);
                }
            }
        };

        if (accumulatedDelay <= 0) {
            executeBatch.run();
        } else {
            Bukkit.getScheduler().runTaskLater(ctx.getPlugin(), executeBatch, accumulatedDelay);
        }

        if (nextDelay >= 0) {
            final int finalNextBatchStart = nextBatchStart;
            final long finalNextDelay = accumulatedDelay + nextDelay;
            if (accumulatedDelay <= 0) {
                scheduleChain(ctx, items, finalNextBatchStart, finalNextDelay);
            } else {
                Bukkit.getScheduler().runTaskLater(ctx.getPlugin(), () ->
                                scheduleChain(ctx, items, finalNextBatchStart, finalNextDelay),
                        accumulatedDelay
                );
            }
        }
    }

    public static long parseDelay(@NotNull String line) {
        String key = ActionRegistry.resolveKey(line, ActionRegistry.LIBB);
        if (key == null) return -1;

        if (!key.equals("delay") && !key.equals("libb:delay")) return -1;

        String text = ActionRegistry.extractText(line, key).trim();
        try {
            long ticks = Long.parseLong(text);
            return Math.max(ticks, 0);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public enum EvaluateMode {
        ALL,
        AT_LEAST_ONE,
        ONE_ONLY
    }

    public static boolean evaluate(Player player, @NotNull List<Expression> expressions, @NotNull EvaluateMode mode) {
        int matched = 0;
        for (Expression expression : expressions) {
            if (evaluate(player, expression.input())) {
                matched++;
            }
        }
        return switch (mode) {
            case ALL -> matched == expressions.size();
            case AT_LEAST_ONE -> matched >= 1;
            case ONE_ONLY -> matched == 1;
        };
    }

    public static boolean evaluate(Player player, @NotNull String input) {
        String[] parts = input.split(" ", 3);
        if (parts.length < 3) return false;

        String left = player != null ? PlaceholderAPI.setPlaceholders(player, parts[0]) : parts[0];
        String op = parts[1];
        String right = player != null ? PlaceholderAPI.setPlaceholders(player, parts[2]) : parts[2];

        try {
            double l = Double.parseDouble(left);
            double r = Double.parseDouble(right);
            return switch (op) {
                case ">" -> l > r;
                case ">=" -> l >= r;
                case "==" -> Math.abs(l - r) < 1e-9;
                case "!=" -> l != r;
                case "<=" -> l <= r;
                case "<" -> l < r;
                default -> false;
            };
        } catch (NumberFormatException ignored) {
        }

        return switch (op) {
            case "==" -> left.equals(right);
            case "!=" -> !left.equals(right);
            default -> false;
        };
    }
}
