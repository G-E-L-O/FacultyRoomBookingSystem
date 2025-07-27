package psgra.sgra.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reserva {

    private final StringProperty docente;
    private final StringProperty aula;
    private final StringProperty curso;
    private final StringProperty fecha;
    private final StringProperty hora;
    private final StringProperty detalle;

    public Reserva(String docente, String aula, String curso, String fecha, String hora, String detalle) {
        this.docente = new SimpleStringProperty(docente);
        this.aula = new SimpleStringProperty(aula);
        this.curso = new SimpleStringProperty(curso);
        this.fecha = new SimpleStringProperty(fecha);
        this.hora = new SimpleStringProperty(hora);
        this.detalle = new SimpleStringProperty(detalle);
    }

    public Reserva(String aula, String curso, String hora, String detalle) {
        this("", aula, curso, "", hora, detalle); // docente y fecha vacíos
    }

    // Getters
    public String getDocente() { return docente.get(); }
    public String getAula()     { return aula.get(); }
    public String getCurso()    { return curso.get(); }
    public String getFecha()    { return fecha.get(); }
    public String getHora()     { return hora.get(); }
    public String getDetalle()  { return detalle.get(); }

    // Properties
    public StringProperty docenteProperty() { return docente; }
    public StringProperty aulaProperty()    { return aula; }
    public StringProperty cursoProperty()   { return curso; }
    public StringProperty fechaProperty()   { return fecha; }
    public StringProperty horaProperty()    { return hora; }
    public StringProperty detalleProperty() { return detalle; }

    // Setters (opcional)
    public void setDocente(String d) { docente.set(d); }
    public void setAula(String a)    { aula.set(a); }
    public void setCurso(String c)   { curso.set(c); }
    public void setFecha(String f)   { fecha.set(f); }
    public void setHora(String h)    { hora.set(h); }
    public void setDetalle(String d) { detalle.set(d); }
}
