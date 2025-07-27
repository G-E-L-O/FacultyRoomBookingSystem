package     psgra.sgra.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import psgra.sgra.servicio.ReservaDAO;
import psgra.sgra.modelo.Reserva;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;

public class ReservasDocenteController {

    @FXML private Label currentDateLabel;
    @FXML private ComboBox<String> viewModeSelector;
    @FXML private GridPane calendarGrid;
    @FXML private ComboBox<String> aulaSelect;
    @FXML private ComboBox<String> horaInicio;
    @FXML private ComboBox<String> horaFin;
    @FXML private TextField cursoFiltro;
    @FXML private DatePicker diaConsulta;
    @FXML private CheckBox soloHorasLibres;
    @FXML private TextField busqueda;
    @FXML private ListView<String> resultadosBusqueda;
    @FXML private ListView<String> disponibilidadResultados;
    @FXML private VBox dayView;
    @FXML private VBox weekView;
    @FXML private VBox hourContainer;
    @FXML private GridPane weekGrid;
    @FXML private Label dayShort;
    @FXML private Label weekShort;
    @FXML private Region lineaRoja;
    @FXML private ScrollPane scrollResultados;
    @FXML private ScrollPane scrollDisponibilidad;


    @FXML private Button btnGuardarDia;
    @FXML private Button btnGuardarSemana;

    private LocalDate selectedDate = LocalDate.now();

    private static final List<String> AULAS = List.of("Aula A", "Aula B", "Aula C");
    private Timeline lineaActualTimeline;
    private Region lineaRojaActual;

    @FXML
    public void initialize() {
        aulaSelect.getItems().add("todas");
        List<String> aulasEnBD = ReservaDAO.obtenerTodasLasAulas();
        aulaSelect.getItems().add("todas");
        aulaSelect.getItems().addAll(aulasEnBD);
        horaInicio.getItems().addAll(generarHoras());
        horaFin.getItems().addAll(generarHoras());
        diaConsulta.setValue(LocalDate.now());
        selectedDate = LocalDate.now();
        viewModeSelector.setValue("Diario");
        lineaRoja.setVisible(false);

        renderCalendar();
        showDayView();

        configurarTooltipLineaRoja();
    }


    private void configurarTooltipLineaRoja() {
        Tooltip tooltip = new Tooltip();
        Tooltip.install(lineaRoja, tooltip);

        lineaRoja.setOnMouseEntered(event -> {
            String horaActual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            tooltip.setText("Hora actual: " + horaActual);
        });

        // Opcional: actualizar dinámicamente si mantienes el mouse encima
        lineaRoja.setOnMouseMoved(event -> {
            String horaActual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            tooltip.setText("Hora actual: " + horaActual);
        });
    }
    private List<String> generarHoras() {
        List<String> horas = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            horas.add(String.format("%02d:00", h));
        }
        return horas;
    }

    @FXML public void movePreviousMonth() {
        selectedDate = selectedDate.minusMonths(1);
        renderCalendar();
    }

    @FXML public void moveNextMonth() {
        selectedDate = selectedDate.plusMonths(1);
        renderCalendar();
    }

    @FXML public void goToToday() {
        selectedDate = LocalDate.now();
        renderCalendar();
    }

    @FXML
    public void changeViewMode() {
        String selected = viewModeSelector.getValue();
        boolean isWeek = "Semanal".equals(selected);
        dayView.setVisible(!isWeek);
        dayView.setManaged(!isWeek);
        weekView.setVisible(isWeek);
        weekView.setManaged(isWeek);
        if (isWeek) {
            showWeekView();
        } else {
            showDayView();
        }
    }

    private void renderCalendar() {
        calendarGrid.getChildren().clear();

        // Agregar iniciales de los días
        String[] dias = {"D", "L", "M", "M", "J", "V", "S"};
        for (int i = 0; i < 7; i++) {
            Label dia = new Label(dias[i]);
            dia.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            dia.setMaxWidth(Double.MAX_VALUE);
            dia.setAlignment(Pos.CENTER); // ← esto centra el texto
            GridPane.setHalignment(dia, HPos.CENTER); // Alineación horizontal explícita
            calendarGrid.add(dia, i, 0);
        }

        currentDateLabel.setText(
                selectedDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()).toUpperCase() +
                        " " + selectedDate.getYear()
        );

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeekIndex = (firstOfMonth.getDayOfWeek().getValue() % 7);
        int totalDays = selectedDate.lengthOfMonth();

        int row = 1;
        int col = dayOfWeekIndex;

        for (int i = 1; i <= totalDays; i++) {
            LocalDate thisDay = selectedDate.withDayOfMonth(i);
            Button dayButton = new Button(String.valueOf(i));
            dayButton.setMinSize(40, 40);
            dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dayButton.getStyleClass().add("calendar-day");

            boolean isToday = thisDay.equals(LocalDate.now());
            boolean isSelected = thisDay.equals(selectedDate);
            boolean hasReserva = !ReservaDAO.obtenerReservasPorFecha(thisDay).isEmpty();

            dayButton.getStyleClass().add("calendar-day");

            if (isToday) dayButton.getStyleClass().add("today");
            if (isSelected) dayButton.getStyleClass().add("selected");
            if (hasReserva) dayButton.getStyleClass().add("reserved");

            LocalDate finalThisDay = thisDay;
            dayButton.setOnAction(e -> {
                selectedDate = finalThisDay;
                renderCalendar();

                // Actualizar título del itinerario
                if (viewModeSelector.getValue().equals("Diario")) {
                    showDayView();
                } else {
                    showWeekView();
                }
            });

            calendarGrid.add(dayButton, col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    public void guardarReservas() {
        String fechaClave = selectedDate.toString();
        List<Reserva> listaOriginal = ReservaDAO.obtenerReservasPorFecha(selectedDate);

        if (listaOriginal.isEmpty()) {
            mostrarAlerta("No hay reservas creadas para esta fecha.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirmar Reservas a Guardar");
        dialog.setHeaderText("Revisa las reservas antes de confirmar.");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/psgra/sgra/reservasDocente.css").toExternalForm());


        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));

        // Lista editable temporal de reservas seleccionadas
        List<Reserva> reservasSeleccionadas = new ArrayList<>(listaOriginal);

        actualizarListaReservasDialog(contenido, reservasSeleccionadas);

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (reservasSeleccionadas.isEmpty()) {
                mostrarAlerta("No se seleccionó ninguna reserva para guardar.");
                return;
            }

            // Placeholder para luego enviar por correo
            System.out.println("Reservas seleccionadas:");
            for (Reserva r : reservasSeleccionadas) {
                System.out.println(r.getHora() + " - " + r.getAula() + ": " + r.getCurso());
            }

            mostrarAlerta("Reservas preparadas para enviar (correo aún no implementado).");
        }
    }

    private void actualizarListaReservasDialog(VBox contenedor, List<Reserva> reservas) {
        contenedor.getChildren().clear();

        for (Reserva r : new ArrayList<>(reservas)) {
            Label info = new Label(r.getHora() + " - " + r.getAula() + ": " + r.getCurso());
            info.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");

            Button eliminar = new Button("❌");
            eliminar.setTooltip(new Tooltip("Eliminar esta reserva del guardado"));
            eliminar.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 14px;");

            eliminar.setOnAction(e -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirmación");
                confirm.setHeaderText("¿Estás seguro de eliminar esta reserva?");
                confirm.setContentText(info.getText());
                Optional<ButtonType> res = confirm.showAndWait();
                if (res.isPresent() && res.get() == ButtonType.OK) {
                    reservas.remove(r);
                    actualizarListaReservasDialog(contenedor, reservas);
                }
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox fila = new HBox(10, info, spacer, eliminar);
            fila.setAlignment(Pos.CENTER_LEFT);
            contenedor.getChildren().add(fila);
        }
    }

    private File generarHorarioSemanalExcel(LocalDate semanaInicio) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Horario Semanal");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Hora");

        for (int d = 0; d < 7; d++) {
            LocalDate dia = semanaInicio.plusDays(d);
            String titulo = dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")) + " " + dia.getDayOfMonth();
            headerRow.createCell(d + 1).setCellValue(titulo);
        }

        for (int h = 0; h < 24; h++) {
            Row row = sheet.createRow(h + 1);
            String hora = String.format("%02d:00", h);
            row.createCell(0).setCellValue(hora);

            for (int d = 0; d < 7; d++) {
                LocalDate dia = semanaInicio.plusDays(d);
                String contenido = "";
                List<Reserva> reservas = ReservaDAO.obtenerReservasPorFecha(dia);
                for (Reserva r : reservas) {
                    if (r.getHora().equals(hora)) {
                        contenido += r.getAula() + ": " + r.getCurso() + "\n";
                    }
                }
                row.createCell(d + 1).setCellValue(contenido.trim());
            }
        }

        try {
            File archivo = File.createTempFile("HorarioSemanal_", ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                workbook.write(fos);
            }
            workbook.close();
            return archivo;
        } catch (IOException e) {
            mostrarAlerta("Error al generar el archivo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void abrirGmailConBorrador(String destinatario, String asunto, String cuerpo) {
        try {
            String uri = "https://mail.google.com/mail/?view=cm&fs=1&to=" +
                    URLEncoder.encode(destinatario, "UTF-8") +
                    "&su=" + URLEncoder.encode(asunto, "UTF-8") +
                    "&body=" + URLEncoder.encode(cuerpo, "UTF-8");

            Desktop.getDesktop().browse(new URI(uri));
        } catch (Exception e) {
            mostrarAlerta("No se pudo abrir Gmail: " + e.getMessage());
        }
    }

    @FXML
    public void enviarHorarioSemanal() {
        LocalDate inicioSemana = selectedDate.minusDays(selectedDate.getDayOfWeek().getValue() % 7);
        File excel = generarHorarioSemanalExcel(inicioSemana);

        if (excel != null && excel.exists()) {
            // Abrir Gmail en navegador con mensaje predeterminado
            abrirGmailConBorrador(
                    "destinatario@gmail.com",
                    "Horario semanal del " + inicioSemana,
                    "Adjunto el archivo del horario semanal. Recuerda arrastrarlo desde la carpeta donde se descargó el archivo:\n\n" + excel.getAbsolutePath()
            );

            mostrarAlerta("El archivo Excel fue generado y se abrió Gmail para que lo puedas enviar manualmente.");
        }
    }

    @FXML
    public void enviarHorarioConAccesoFacilAlArchivo() {
        LocalDate inicioSemana = selectedDate.minusDays(selectedDate.getDayOfWeek().getValue() % 7);
        File archivo = generarHorarioSemanalExcel(inicioSemana);

        if (archivo != null && archivo.exists()) {
            // Abrir carpeta que contiene el archivo
            try {
                Desktop.getDesktop().open(archivo.getParentFile());
            } catch (IOException e) {
                mostrarAlerta("No se pudo abrir la carpeta: " + e.getMessage());
            }

            // Abrir Gmail prellenado
            String asunto = "Horario semanal del " + inicioSemana;
            String cuerpo = "Hola,\n\nAdjunto el horario semanal en formato Excel.\n\n"
                    + "Ubicación del archivo: " + archivo.getAbsolutePath() + "\n"
                    + "Por favor, adjunta el archivo antes de enviar.\n\nSaludos.";
            abrirGmailConBorrador("ejemplo@gmail.com", asunto, cuerpo);

            mostrarAlerta("Se ha generado el archivo y se abrió Gmail y la carpeta para facilitar el envío.");
        } else {
            mostrarAlerta("No se pudo generar el archivo.");
        }
    }

    private void showDayView() {
        hourContainer.getChildren().clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        String nombreDia = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        String fechaFormateada = selectedDate.format(formatter);
        dayShort.setText(nombreDia + " " + fechaFormateada);
        String key = selectedDate.toString();

        LocalTime now = LocalTime.now();
        boolean isToday = selectedDate.equals(LocalDate.now());

        for (int h = 0; h < 24; h++) {
            String hora = String.format("%02d:00", h);

            HBox hourRow = new HBox(10);
            hourRow.getStyleClass().add("hour-row");

            Label labelHora = new Label(hora);
            labelHora.getStyleClass().add("hour-label");

            HBox reservasBox = new HBox();
            reservasBox.setSpacing(6);

            ScrollPane scroll = new ScrollPane(reservasBox);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setFitToHeight(true);
            scroll.setFitToWidth(false);
            scroll.setPrefHeight(40);

            // Agregar reservas existentes
            List<Reserva> reservasDelDia = ReservaDAO.obtenerReservasPorFecha(selectedDate);
            for (Reserva r : reservasDelDia) {
                    if (r.getHora().equals(hora)) {
                        Label resLabel = new Label(r.getAula() + ": " + r.getCurso());
                        resLabel.getStyleClass().add("reserva-item");
                        reservasBox.getChildren().add(resLabel);
                        resLabel.setOnMouseClicked(event -> {
                            abrirPopupEdicion(r, selectedDate);
                            event.consume();
                        });
                    }
                }


            final String horaFinal = hora;
            hourRow.setOnMouseClicked(event -> {
                abrirPopupReserva(selectedDate, horaFinal);
            });

            hourRow.getChildren().addAll(labelHora, scroll);
            hourContainer.getChildren().add(hourRow);

            // Línea separadora
            Region separator = new Region();
            separator.getStyleClass().add("hour-separator");
            hourContainer.getChildren().add(separator);

            // Línea roja si es la hora actual
            if (isToday) {
                double minutosDelDia = now.getHour() * 60 + now.getMinute();
                double porcentaje = minutosDelDia / (24 * 60);

                Region lineaRoja = new Region();
                lineaRoja.getStyleClass().add("linea-actual");



                Platform.runLater(() -> {
                    double alturaContenedor = hourContainer.getHeight();
                    double posicionY = alturaContenedor * porcentaje;

                    lineaRoja.setTranslateY(posicionY);

                });
            }
        }
        iniciarActualizacionLineaActual();
    }

    private void iniciarActualizacionLineaActual() {
        if (!selectedDate.equals(LocalDate.now())) {
            lineaRoja.setVisible(false);
            if (lineaActualTimeline != null) lineaActualTimeline.stop();
            return;
        }

        lineaRoja.setVisible(true);

        if (lineaActualTimeline != null) lineaActualTimeline.stop();

        lineaActualTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> actualizarLineaRoja()),
                new KeyFrame(Duration.seconds(1))
        );
        lineaActualTimeline.setCycleCount(Animation.INDEFINITE);
        lineaActualTimeline.play();
    }


    private void actualizarLineaRoja() {
        LocalTime ahora = LocalTime.now();
        double minutos = ahora.getHour() * 60 + ahora.getMinute() + ahora.getSecond() / 60.0;
        double porcentaje = minutos / (24 * 60);

        Platform.runLater(() -> {
            double altura = hourContainer.getHeight();
            double posicionY = altura * porcentaje;

            lineaRoja.setTranslateY(posicionY);
            lineaRoja.setPrefHeight(2);
            lineaRoja.setVisible(true);
        });
    }



    private void showWeekView() {
        weekGrid.getChildren().clear();
        weekGrid.getColumnConstraints().clear();
        weekGrid.getRowConstraints().clear();

        weekShort.setText("Semana de " + selectedDate);

        // Configurar columnas
        ColumnConstraints horaCol = new ColumnConstraints();
        horaCol.setPercentWidth(10);
        weekGrid.getColumnConstraints().add(horaCol);
        for (int i = 0; i < 7; i++) {
            ColumnConstraints dayCol = new ColumnConstraints();
            dayCol.setPercentWidth(90.0 / 7);
            weekGrid.getColumnConstraints().add(dayCol);
        }

        // Encabezado de días
        LocalDate startOfWeek = selectedDate.minusDays(selectedDate.getDayOfWeek().getValue() % 7);
        for (int i = 0; i < 7; i++) {
            LocalDate dia = startOfWeek.plusDays(i);
            String nombreDia = dia.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, new Locale("es", "ES")).toUpperCase();
            Label dayHeader = new Label(nombreDia + " " + dia.getDayOfMonth());
            dayHeader.getStyleClass().add("week-day-header");
            dayHeader.setMaxWidth(Double.MAX_VALUE);
            dayHeader.setAlignment(Pos.CENTER);
            weekGrid.add(dayHeader, i + 1, 0);
        }

        // Filas por cada hora
        for (int h = 0; h < 24; h++) {
            String hora = String.format("%02d:00", h);

            // Columna de horas
            Label horaLabel = new Label(hora);
            horaLabel.getStyleClass().add("week-hour-label");
            weekGrid.add(horaLabel, 0, h + 1);

            // Columnas de días
            for (int d = 0; d < 7; d++) {
                LocalDate dia = startOfWeek.plusDays(d);
                VBox celda = new VBox();
                celda.getStyleClass().add("celda-semana");

                String key = dia.toString();
                List<Reserva> reservasDia = ReservaDAO.obtenerReservasPorFecha(dia);
                for (Reserva r : reservasDia) {
                        if (r.getHora().equals(hora)) {
                            Label resLabel = new Label(r.getAula() + ": " + r.getCurso());
                            resLabel.getStyleClass().add("reserva-item-semana");
                            resLabel.setOnMouseClicked(event -> {
                                abrirPopupEdicion(r, dia);
                                event.consume();
                            });
                            celda.getChildren().add(resLabel);
                        }
                    }


                final String horaFinal = hora;
                final LocalDate diaFinal = dia;
                celda.setOnMouseClicked(event -> {
                    abrirPopupReserva(diaFinal, horaFinal);
                });

                weekGrid.add(celda, d + 1, h + 1);
            }
        }
    }



    private void abrirPopupReserva(LocalDate dia, String hora) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Nueva Reserva");
        String horaFin;
        try {
            int horaInt = Integer.parseInt(hora.split(":")[0]);
            int siguiente = (horaInt + 1) % 24;
            horaFin = String.format("%02d:00", siguiente);
        } catch (Exception e) {
            horaFin = "??:??";
        }

        dialog.setHeaderText("Reserva para el " + dia + " de " + hora + " a " + horaFin);

        // Contenido del diálogo
        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));

        ComboBox<String> aulaBox = new ComboBox<>();
        aulaBox.getItems().addAll(AULAS);
        aulaBox.setPromptText("Selecciona Aula");

        TextField cursoField = new TextField();
        cursoField.setPromptText("Curso");

        TextArea detalleArea = new TextArea();
        detalleArea.setPromptText("Detalle (opcional)");
        detalleArea.setPrefRowCount(3);

        contenido.getChildren().addAll(new Label("Aula:"), aulaBox, new Label("Curso:"), cursoField, new Label("Detalle:"), detalleArea);

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String aula = aulaBox.getValue();
            String curso = cursoField.getText();
            String detalle = detalleArea.getText();

            if (aula == null || curso.isEmpty()) {
                mostrarAlerta("Debes completar el aula y el curso.");

                // Volver a abrir el diálogo de reserva
                Platform.runLater(() -> abrirPopupReserva(dia, hora));
                return;
            }

            String diaKey = dia.toString();

            // Verificar colisión
            if (ReservaDAO.existeReserva(dia, aula, hora)) {
                mostrarAlerta("Ya hay una reserva para esa aula y hora.");
                return;
            }

            // Guardar reserva
            String fechaStr = dia.toString();
            Reserva nueva = new Reserva("", aula, curso, fechaStr, hora, detalle);
            ReservaDAO.guardarReserva(dia, nueva);

            renderCalendar();
            showDayView();
            showWeekView();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML public void consultarDisponibilidad() {
        disponibilidadResultados.getItems().clear();
        String aulaFiltro = aulaSelect.getValue();
        String curso = cursoFiltro.getText().toLowerCase();
        LocalDate dia = diaConsulta.getValue();
        String horaIni = horaInicio.getValue();
        String horaFinStr = horaFin.getValue();
        boolean soloLibres = soloHorasLibres.isSelected();

        if (horaIni == null || horaFinStr == null || dia == null) return;

        int hi = Integer.parseInt(horaIni.split(":" )[0]);
        int hf = Integer.parseInt(horaFinStr.split(":" )[0]);

        List<String> disponibles = new ArrayList<>();
        for (String aula : AULAS) {
            boolean libre = true;
            for (int h = hi; h <= hf; h++) {
                String hora = String.format("%02d:00", h);
                List<Reserva> reservasDelDia = ReservaDAO.obtenerReservasPorFecha(dia);
                for (Reserva r : reservasDelDia) {
                        if (r.getAula().equals(aula) && r.getHora().equals(hora)) {
                            if (soloLibres || (!curso.isEmpty() && !r.getCurso().toLowerCase().contains(curso))) {
                                libre = false;
                            }
                        }
                    }

            }
            if (libre && (aulaFiltro.equals("todas") || aulaFiltro.equals(aula))) {
                disponibles.add(aula);
            }
        }
        for (String a : disponibles) {
            disponibilidadResultados.getItems().add(a + " disponible");
        }
        boolean hay = !disponibilidadResultados.getItems().isEmpty();
        scrollDisponibilidad.setVisible(hay);
        scrollDisponibilidad.setManaged(hay);

        int cantidad = Math.min(disponibilidadResultados.getItems().size(), 4);
        disponibilidadResultados.setPrefHeight(cantidad * 30);

    }



    @FXML
    public void buscarReservas() {
        resultadosBusqueda.getItems().clear();
        String query = busqueda.getText().toLowerCase();

        List<Reserva> resultados = ReservaDAO.buscarReservas(query);

        for (Reserva r : resultados) {
            resultadosBusqueda.getItems().add(r.getHora() + " - " + r.getAula() + ": " + r.getCurso());
        }

        boolean hayResultados = !resultados.isEmpty();
        scrollResultados.setVisible(hayResultados);
        scrollResultados.setManaged(hayResultados);
        resultadosBusqueda.setPrefHeight(Math.min(resultados.size(), 4) * 30);
    }



    private void abrirPopupEdicion(Reserva reserva, LocalDate fecha) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Reserva");

        // Crear campos para editar
        TextField campoCurso = new TextField(reserva.getCurso());
        ComboBox<String> campoAula = new ComboBox<>();
        campoAula.getItems().addAll(AULAS);
        campoAula.setValue(reserva.getAula());
        TextField campoDetalle = new TextField(reserva.getDetalle());

        VBox contenido = new VBox(10,
                new Label("Curso:"), campoCurso,
                new Label("Aula:"), campoAula,
                new Label("Detalle:"), campoDetalle
        );
        contenido.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(contenido);

        ButtonType eliminarBtn = new ButtonType("Eliminar", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, eliminarBtn, ButtonType.CANCEL);

        Optional<ButtonType> resultado = dialog.showAndWait();

        if (resultado.isPresent()) {
            if (resultado.get() == ButtonType.OK) {
                Reserva nueva = new Reserva(
                        campoAula.getValue(),
                        campoCurso.getText(),
                        reserva.getHora(),
                        campoDetalle.getText()
                );
                ReservaDAO.actualizarReserva(fecha, reserva.getHora(), reserva.getAula(), nueva);
                showDayView();
                showWeekView();
            } else if (resultado.get().getText().equals("Eliminar")) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Eliminar Reserva");
                confirm.setHeaderText("¿Estás seguro de eliminar esta reserva?");
                confirm.setContentText(reserva.getHora() + " - " + reserva.getAula() + ": " + reserva.getCurso());

                Optional<ButtonType> confirmacion = confirm.showAndWait();
                if (confirmacion.isPresent() && confirmacion.get() == ButtonType.OK) {
                    ReservaDAO.eliminarReserva(fecha, reserva.getHora(), reserva.getAula());
                    showDayView();
                    showWeekView();
                }
            }
        }
    }




}
