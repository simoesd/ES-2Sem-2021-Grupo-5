package main_package;

public class Line {
    private int id, nom_class, loc_class, wmc_class, loc_method, cyclo_method;
    private String pkg, cls, method;
    private Boolean is_god, is_long;
    
    public Line(int id, String pkg, String cls, String method, int nom_class, int loc_class, int wmc_class, Boolean is_god, int loc_method, int cyclo_method, Boolean is_long) {
        this.id = id;
        this.nom_class = nom_class;
        this.loc_class = loc_class;
        this.wmc_class = wmc_class;
        this.loc_method = loc_method;
        this.pkg = pkg;
        this.cls = cls;
        this.method = method;
        this.is_god = is_god;
        this.cyclo_method = cyclo_method;
        this.is_long = is_long;
    }

    public int getId() {
        return id;
    }

    public int getNom_class() {
        return nom_class;
    }

    public int getLoc_class() {
        return loc_class;
    }

    public int getWmc_class() {
        return wmc_class;
    }

    public int getLoc_method() {
        return loc_method;
    }

    public int getCyclo_method() {
        return cyclo_method;
    }

    public String getPkg() {
        return pkg;
    }

    public String getCls() {
        return cls;
    }

    public String getMethod() {
        return method;
    }

    public Boolean getIs_god() {
        return is_god;
    }

    public Boolean getIs_long() {
        return is_long;
    }

    public String[] toArray() {
        String[] temp = {String.valueOf(id), String.valueOf(nom_class), String.valueOf(loc_class), String.valueOf(wmc_class), String.valueOf(loc_method), String.valueOf(cyclo_method), pkg, cls, method, is_god.toString(), is_long.toString()};
        return temp;
    }
    
    
}

