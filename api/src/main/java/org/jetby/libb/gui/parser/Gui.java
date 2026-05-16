package org.jetby.libb.gui.parser;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetby.libb.action.record.ActionBlock;
import org.jetby.libb.action.record.Expression;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Gui {


    private String id;
    private String title;
    private int size;
    private List<String> command;
    private List<Expression> preOpenExpressions;
    private ActionBlock onOpen;
    private ActionBlock onClose;
    private List<Item> items;

}
