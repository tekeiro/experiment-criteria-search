package org.keirobm.usage_jpacriteriabuilder;

public class Author {

    private Integer id;
    private String name;
    private Editorial editorial;

    public Author(Integer id, String name, Editorial editorial) {
        this.id = id;
        this.name = name;
        this.editorial = editorial;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }
}
