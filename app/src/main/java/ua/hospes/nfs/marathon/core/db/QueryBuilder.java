package ua.hospes.nfs.marathon.core.db;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
    private static final String LOG_TAG = "QueryBuilder";

    public static final String AND = " AND ";
    public static final String OR = " OR ";
    public static final String AS = " AS ";

    private String table = null;
    private Map<String, String> projectionMap = new HashMap<>();
    private StringBuilder selection = new StringBuilder();
    private List<String> selectionArgs = new ArrayList<>();


    public QueryBuilder(String table) {
        if (TextUtils.isEmpty(table)) throw new RuntimeException("Specify table name");
        this.table = table;
    }


    /**
     * Reset any internal state, allowing this builder to be recycled.
     */
    public QueryBuilder reset() {
        table = null;
        selection.setLength(0);
        selectionArgs.clear();
        return this;
    }

    /**
     * Append the given selection clause to the internal state. Each clause is
     * surrounded with parenthesis and combined using {@code AND}.
     */
    public QueryBuilder where(String selection, String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException("Valid selection required when including arguments=");
            }
            return this;
        }

        this.selection.append("(").append(selection).append(")");
        if (selectionArgs != null) {
            Collections.addAll(this.selectionArgs, selectionArgs);
        }
        return this;
    }

    public QueryBuilder mapToTable(String column, String table) {
        projectionMap.put(column, table + "." + column);
        return this;
    }

    public QueryBuilder map(String fromColumn, String toClause) {
        projectionMap.put(fromColumn, toClause + AS + fromColumn);
        return this;
    }


    public QueryBuilder and() {
        selection.append(AND);
        return this;
    }

    public QueryBuilder or() {
        selection.append(OR);
        return this;
    }


    public String getTable() {
        return table;
    }

    /**
     * Return selection string for current internal state.
     *
     * @see #getSelectionArgs()
     */
    public String getSelection() {
        return selection.toString();
    }

    /**
     * Return selection string for current internal state.
     *
     * @see #getSelectionArgs()
     */
    public String getFullSelection() {
        if (selection.length() == 0) {
            return selection.insert(0, "SELECT * FROM " + table).toString();
        } else {
            return selection.insert(0, "SELECT * FROM " + table + " WHERE ").toString();
        }
    }

    /**
     * Return selection arguments for current internal state.
     *
     * @see #getSelection()
     */
    public String[] getSelectionArgs() {
        return selectionArgs.toArray(new String[selectionArgs.size()]);
    }

    private void mapColumns(String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            final String target = projectionMap.get(columns[i]);
            if (target != null) {
                columns[i] = target;
            }
        }
    }


    @Override
    public String toString() {
        return "SelectionBuilder[table=" + table + ", selection=" + getSelection() + ", selectionArgs=" + Arrays.toString(getSelectionArgs()) + "]";
    }
}