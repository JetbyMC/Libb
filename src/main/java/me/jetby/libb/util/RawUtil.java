package me.jetby.libb.util;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RawUtil {

    @Nullable
    public static String getResult(String link) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(link).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) sb.append(line);
                return sb.toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static String getResult(String link, int timeout) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(link).openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) sb.append(line);
                return sb.toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
