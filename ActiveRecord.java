import java.lang.reflect.Field;

/**
 * This class will agnosticaly get data from a simple classes and export SQL
 *
 * How to use 
 * 1 -> Extends this class 
 * 2 -> Create a final String called _tablename 
 * 3 -> Take care with the id property. It is used here
 *
 * This class was created with the following documentation
 * https://www.oracle.com/technical-resources/articles/java/javareflection.html
 *
 * @author Artur Henrique Welp ahwelp@universo.univates.br
 */
public abstract class ActiveRecord {
    
    /**
     * exportInsertSql
     *
     * This method will export a create string
     *
     * @return String
     */
    public String exportInsertSql() {

        String table = "";
        String fields = "";
        String values = "";

        try {
            Class cls = Class.forName(this.getClass().toString().replace("class ", ""));
            Field fieldlist[] = cls.getDeclaredFields();

            for (int i = 0; i < fieldlist.length; i++) {

                Field fld = fieldlist[i];
                if (fld.getName().equals("_tablename")) {
                    table = fld.get(this).toString();
                    continue;
                }

                switch (fld.getType().toString()) {
                    case "int":
                        fields += fld.getName() + ",";
                        values += fld.get(this) + ",";
                        break;
                    case "class java.lang.String":
                        fields += fld.getName() + ",";
                        values += "'" + fld.get(this) + "',";
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) { return ""; }

        fields = fields.substring(0, fields.length() - 1);
        values = values.substring(0, values.length() - 1);
        
        if (table.equals("")) { return ""; }
        
        return "INSERT INTO " + table + " (" + fields + ") VALUES (" + values + ");";

    }

    /**
     * exportUpdateSql
     *
     * This class wil export a Update SQL 
     * If there is not a id on the class, the return will be a empty String
     *
     * @return String
     */
    public String exportUpdateSql() {

        int id = 0;
        String table = "";
        String update = "";

        try {
            Class cls = Class.forName(this.getClass().toString().replace("class ", ""));
            Field fieldlist[] = cls.getDeclaredFields();

            for (int i = 0; i < fieldlist.length; i++) {

                Field fld = fieldlist[i];
                if (fld.getName().equals("_tablename")) {
                    table = fld.get(this).toString();
                    continue;
                }
                if (fld.getName().equals("id")) {
                    id = Integer.parseInt(fld.get(this).toString());
                    continue;
                }

                switch (fld.getType().toString()) {
                    case "int":
                        update += fld.getName() + " = ";
                        update += fld.get(this) + ",";
                        break;
                    case "class java.lang.String":
                        update += fld.getName() + " = ";
                        update += "'" + fld.get(this) + "',";
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) { return ""; }

        update = update.substring(0, update.length() - 1);
        if (id == 0 || table.equals("")) { return ""; }
        
        return "UPDATE " + table + " SET " + update + " WHERE id = " + id + ";";

    }

    /**
     * exportDeleteSql
     *
     * This class wil export a Delete SQL     *
     * If there is not a id on the class, the return will be a empty String
     *
     * @return String
     */
    public String exportDeleteSql() {
        
        int id = 0;
        String table = "";

        try {
            Class cls = Class.forName(this.getClass().toString().replace("class ", ""));

            Field fieldlist[] = cls.getDeclaredFields();
            for (int i = 0; i < fieldlist.length; i++) {

                Field fld = fieldlist[i];
                if (fld.getName().equals("_tablename")) {
                    table = fld.get(this).toString();
                    continue;
                }
                if (fld.getName().equals("id")) {
                    id = Integer.parseInt(fld.get(this).toString());
                    continue;
                }
            }
        } catch (Exception e) { return ""; }

        if (id == 0 || table.equals("")) { return ""; }
        
        return "DELET FROM " + table + " WHERE id = " + id + ";";
    }
}
