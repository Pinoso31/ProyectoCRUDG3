package com.example.proyectocrudg3.controlller;

import com.example.proyectocrudg3.database.ConexionBD;
import com.example.proyectocrudg3.model.Categoria;
import com.example.proyectocrudg3.model.Marca;
import com.example.proyectocrudg3.model.Producto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductosController {


    //Gestion de productos

    @FXML
    private TextField txtCodigoProd;

    @FXML
    private TextField txtPrecioProd;

    @FXML
    private ComboBox<String> cmbCategoriaProd;

    @FXML
    private DatePicker dtpFechaProd;

    @FXML
    private TextField txtNombreProd;

    @FXML
    private TextField txtStockProd;


    @FXML
    private ComboBox<String> cmbMarcaProd;

    @FXML
    private TextField txtDescripcionProd;

    //Busqueda y filtros
    @FXML
    private TextField txtCodigoNombre;

    @FXML
    private DatePicker dtpDesde;

    @FXML
    private DatePicker dtpHasta;

    @FXML
    private TextField txtPrecioMinimo;

    @FXML
    private TextField txtPrecioMaximo;

    @FXML
    private ComboBox<String> cmbCategoriaFiltro;

    @FXML
    private TableView<Producto> tblProducto;

    @FXML
    private CheckBox chkProductosExistentes;

    //Columnas datos

    @FXML
    private TableColumn<Producto, Integer> colCodigoProd;

    @FXML
    private TableColumn<Producto, String> colNombreProd;

    @FXML
    private TableColumn<Producto, String> colCategoriaProd;

    @FXML
    private TableColumn<Producto, String> colMarcaProd;

    @FXML
    private TableColumn<Producto, Double> colPrecioProd;

    @FXML
    private TableColumn<Producto, Double>colStockProd;

    @FXML
    private ComboBox < String > cmbOrdenarFiltro;

    @FXML
    private TableColumn<Producto, LocalDate>colFechaCreacionProd;

    //Botones
    @FXML private Button btnBuscar;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnActualizar;
    @FXML private Button btnActualizarRegistro;
    @FXML private Button btnEliminar;

    private final Map<String, Integer> categoriasIds = new HashMap<>();
    private final Map<String, Integer> marcasIds = new HashMap<>();

    private final ObservableList<Producto> productos =
            FXCollections.observableArrayList();


    @FXML
    public void initialize() {

        configurarTabla();
        tblProducto.setItems(productos);

        cargarCombos();

        cmbOrdenarFiltro.getItems().addAll(
                "Código",
                "Nombre",
                "Precio menor a mayor",
                "Precio mayor a menor",
                "Stock mayor a menor",
                "Fecha reciente"
        );

        cmbOrdenarFiltro.setValue("Código");

        tblProducto.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        cargarProductoSeleccionado(newValue);
                    }
                });

        txtCodigoNombre.textProperty().addListener(
                (obs, anterior, nuevo) -> cargarDatos());

        txtPrecioMinimo.textProperty().addListener(
                (obs, anterior, nuevo) -> cargarDatos());

        txtPrecioMaximo.textProperty().addListener(
                (obs, anterior, nuevo) -> cargarDatos());

        cmbCategoriaFiltro.setOnAction(e -> cargarDatos());
        cmbOrdenarFiltro.setOnAction(e -> cargarDatos());
        dtpDesde.setOnAction(e -> cargarDatos());
        dtpHasta.setOnAction(e -> cargarDatos());
        chkProductosExistentes.setOnAction(e -> cargarDatos());

        cargarDatos();
    }

    // CONFIGURAR TABLA

    public void configurarTabla() {

        colCodigoProd.setCellValueFactory(
                new PropertyValueFactory<>("codigo"));

        colNombreProd.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        colCategoriaProd.setCellValueFactory(
                new PropertyValueFactory<>("categoria"));

        colMarcaProd.setCellValueFactory(
                new PropertyValueFactory<>("marca"));

        colPrecioProd.setCellValueFactory(
                new PropertyValueFactory<>("precio"));

        colStockProd.setCellValueFactory(
                new PropertyValueFactory<>("stock"));

        colFechaCreacionProd.setCellValueFactory(
                new PropertyValueFactory<>("fechaCreacion"));
    }


    // CARGAR CATEGORIAS Y MARCAS

    private void cargarCombos() {

        String sqlCategorias =
                "SELECT id_categoria, nombre " +
                        "FROM catalogos.categorias WHERE estado = 1 " +
                        "ORDER BY nombre";

        String sqlMarcas =
                "SELECT id_marca, nombre " +
                        "FROM catalogos.marcas WHERE estado = 1 " +
                        "ORDER BY nombre";

        categoriasIds.clear();
        marcasIds.clear();

        cmbCategoriaProd.getItems().clear();
        cmbMarcaProd.getItems().clear();
        cmbCategoriaFiltro.getItems().clear();
        cmbCategoriaFiltro.getItems().add("Todas");

        try (Connection connection = ConexionBD.getConnection()) {

            try (PreparedStatement statement =
                         connection.prepareStatement(sqlCategorias);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    String nombre = resultSet.getString("nombre");
                    int id = resultSet.getInt("id_categoria");

                    categoriasIds.put(nombre, id);
                    cmbCategoriaProd.getItems().add(nombre);
                    cmbCategoriaFiltro.getItems().add(nombre);
                }
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sqlMarcas);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    String nombre = resultSet.getString("nombre");
                    int id = resultSet.getInt("id_marca");

                    marcasIds.put(nombre, id);
                    cmbMarcaProd.getItems().add(nombre);
                }
            }

            cmbCategoriaFiltro.setValue("Todas");

        } catch (SQLException ex) {
            ex.printStackTrace();

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error al cargar ComboBox",
                    ex.getMessage()
            );
        }
    }

    // CARGAR PRODUCTO SELECCIONADO


    private void cargarProductoSeleccionado(Producto p) {

        txtCodigoProd.setText(p.getCodigoProducto());
        txtNombreProd.setText(p.getNombreProducto());
        txtDescripcionProd.setText(p.getDescripcionProducto());
        txtPrecioProd.setText(String.valueOf(p.getPrecioProducto()));
        txtStockProd.setText(String.valueOf(p.getStockProducto()));

        cmbCategoriaProd.setValue(p.getCategoria());
        cmbMarcaProd.setValue(p.getMarca());

        dtpFechaProd.setValue(
                p.getFecha_ingresoProducto().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        );

    }

    @FXML
    public void guardarProducto(ActionEvent actionEvent) {

        if (!validarFormulario()) {
            return;
        }

        String sql =
                "INSERT INTO catalogos.productos " +
                        "(codigo, nombre, descripcion, precio, stock, " +
                        "fecha_creacion, id_categoria, id_marca, estado) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";

        try (
                Connection connection = ConexionBD.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, txtCodigoProd.getText().trim());
            statement.setString(2, txtNombreProd.getText().trim());
            statement.setString(3, txtDescripcionProd.getText().trim());

            statement.setBigDecimal(4,
                    new BigDecimal(txtPrecioProd.getText().trim()));

            statement.setInt(5,
                    Integer.parseInt(txtStockProd.getText().trim()));

            statement.setDate(6,
                    Date.valueOf(dtpFechaProd.getValue() != null
                            ? dtpFechaProd.getValue()
                            : LocalDate.now()));

            statement.setInt(7,
                    categoriasIds.get(cmbCategoriaProd.getValue()));

            statement.setInt(8,
                    marcasIds.get(cmbMarcaProd.getValue()));

            int filas = statement.executeUpdate();

            if (filas > 0) {

                limpiarFormulario();
                cargarDatos();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Registro exitoso",
                        "Producto guardado",
                        "El producto se ha guardado correctamente."
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo guardar",
                    ex.getMessage()
            );
        }
    }

    // Metodo actulizar productos

    @FXML
    public void actualizarProducto() {

        Producto seleccionado =
                tblProducto.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Advertencia",
                    "No hay producto seleccionado",
                    "Seleccione un producto de la tabla."
            );
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Actualizar");
        confirmacion.setHeaderText("Confirmar actualización");
        confirmacion.setContentText(
                "¿Está seguro de actualizar el producto seleccionado?"
        );

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        String sql =
                "UPDATE catalogos.productos SET " +
                        "codigo = ?, nombre = ?, descripcion = ?, " +
                        "precio = ?, stock = ?, fecha_creacion = ?, " +
                        "id_categoria = ?, id_marca = ? " +
                        "WHERE id_producto = ? AND estado = 1";

        try (
                Connection connection = ConexionBD.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, txtCodigoProd.getText().trim());
            statement.setString(2, txtNombreProd.getText().trim());
            statement.setString(3, txtDescripcionProd.getText().trim());

            statement.setBigDecimal(4,
                    new BigDecimal(txtPrecioProd.getText().trim()));

            statement.setInt(5,
                    Integer.parseInt(txtStockProd.getText().trim()));

            statement.setDate(6,
                    Date.valueOf(dtpFechaProd.getValue() != null
                            ? dtpFechaProd.getValue()
                            : LocalDate.now()));

            statement.setInt(7,
                    categoriasIds.get(cmbCategoriaProd.getValue()));

            statement.setInt(8,
                    marcasIds.get(cmbMarcaProd.getValue()));

            statement.setInt(9,
                    seleccionado.getId_producto());

            int filas = statement.executeUpdate();

            if (filas > 0) {

                limpiarFormulario();
                cargarDatos();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Registro actualizado",
                        "Producto actualizado",
                        "El producto se ha actualizado correctamente."
                );

            } else {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Advertencia",
                        "Producto no actualizado",
                        "El producto no existe o está inactivo."
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo actualizar",
                    ex.getMessage()
            );
        }
    }


    // Metodo eliminar producto

    @FXML
    public void eliminarProducto() {

        Producto seleccionado =
                tblProducto.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Advertencia",
                    "No hay producto seleccionado",
                    "Seleccione un producto de la tabla para eliminarlo."
            );
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar");
        confirmacion.setHeaderText("Confirmar eliminación");
        confirmacion.setContentText(
                "¿Está seguro de desactivar el producto seleccionado?"
        );

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        String sql =
                "UPDATE catalogos.productos SET estado = 0 " +
                        "WHERE id_producto = ? AND estado = 1";

        try (
                Connection connection = ConexionBD.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1,
                    seleccionado.getId_producto());

            int filas = statement.executeUpdate();

            if (filas > 0) {

                limpiarFormulario();
                cargarDatos();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Registro eliminado",
                        "Producto desactivado",
                        "El producto se ha desactivado correctamente."
                );

            } else {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Advertencia",
                        "Producto no eliminado",
                        "El producto ya está inactivo o no existe."
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo eliminar",
                    ex.getMessage()
            );
        }
    }

    // CARGAR PRODUCTOS Y APLICAR FILTROS

    @FXML
    public void cargarDatos() {

        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.nombre AS categoria, " +
                        "m.nombre AS marca " +
                        "FROM catalogos.productos p " +
                        "INNER JOIN catalogos.categorias c " +
                        "ON p.id_categoria = c.id_categoria " +
                        "INNER JOIN catalogos.marcas m " +
                        "ON p.id_marca = m.id_marca " +
                        "WHERE p.estado = 1"
        );

        String busqueda = txtCodigoNombre.getText().trim();
        String minimo = txtPrecioMinimo.getText().trim();
        String maximo = txtPrecioMaximo.getText().trim();

        String categoriaFiltro = cmbCategoriaFiltro.getValue();

        Integer idCategoriaFiltro =
                categoriasIds.get(categoriaFiltro);

        BigDecimal precioMinimo = null;
        BigDecimal precioMaximo = null;

        try {
            if (!minimo.isEmpty()) {
                precioMinimo = new BigDecimal(minimo);
            }

            if (!maximo.isEmpty()) {
                precioMaximo = new BigDecimal(maximo);
            }
        } catch (NumberFormatException ex) {
            // Esperar hasta que el usuario escriba un número válido
            return;
        }

        if (!busqueda.isEmpty()) {
            sql.append(
                    " AND (p.codigo ILIKE ? OR p.nombre ILIKE ?)"
            );
        }

        if (idCategoriaFiltro != null) {
            sql.append(" AND p.id_categoria = ?");
        }

        if (dtpDesde.getValue() != null) {
            sql.append(" AND p.fecha_creacion >= ?");
        }

        if (dtpHasta.getValue() != null) {
            sql.append(" AND p.fecha_creacion <= ?");
        }

        if (precioMinimo != null) {
            sql.append(" AND p.precio >= ?");
        }

        if (precioMaximo != null) {
            sql.append(" AND p.precio <= ?");
        }

        if (chkProductosExistentes.isSelected()) {
            sql.append(" AND p.stock > 0");
        }

        // Ordenar resultados
        String orden = cmbOrdenarFiltro.getValue();

        if ("Nombre".equals(orden)) {
            sql.append(" ORDER BY p.nombre ASC");

        } else if ("Precio menor a mayor".equals(orden)) {
            sql.append(" ORDER BY p.precio ASC");

        } else if ("Precio mayor a menor".equals(orden)) {
            sql.append(" ORDER BY p.precio DESC");

        } else if ("Stock mayor a menor".equals(orden)) {
            sql.append(" ORDER BY p.stock DESC");

        } else if ("Fecha reciente".equals(orden)) {
            sql.append(" ORDER BY p.fecha_creacion DESC");

        } else {
            sql.append(" ORDER BY p.codigo ASC");
        }

        try (
                Connection connection = ConexionBD.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql.toString())
        ) {

            int posicion = 1;

            if (!busqueda.isEmpty()) {
                statement.setString(posicion++, "%" + busqueda + "%");
                statement.setString(posicion++, "%" + busqueda + "%");
            }

            if (idCategoriaFiltro != null) {
                statement.setInt(posicion++, idCategoriaFiltro);
            }

            if (dtpDesde.getValue() != null) {
                statement.setDate(posicion++,
                        Date.valueOf(dtpDesde.getValue()));
            }

            if (dtpHasta.getValue() != null) {
                statement.setDate(posicion++,
                        Date.valueOf(dtpHasta.getValue()));
            }

            if (precioMinimo != null) {
                statement.setBigDecimal(posicion++, precioMinimo);
            }

            if (precioMaximo != null) {
                statement.setBigDecimal(posicion++, precioMaximo);
            }

            ObservableList<Producto> datos =
                    FXCollections.observableArrayList();

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Producto p = new Producto(
                            resultSet.getInt("id_producto"),
                            resultSet.getString("codigo"),
                            resultSet.getString("nombre"),
                            resultSet.getString("descripcion"),
                            resultSet.getString("precio"),
                            resultSet.getString("categoria"),
                            resultSet.getDouble("marca"),
                            resultSet.getInt("stock"),
                            resultSet.getDate("fecha_creacion"),
                            resultSet.getInt("id_marca"),
                            resultSet.getInt("id_categoria"),
                            resultSet.getString("nombreCategoria"),
                            resultSet.getString("nombreMarca"),
                            resultSet.getBoolean("estado")
                    );

                    datos.add(p);
                }
            }

            productos.setAll(datos);

        } catch (SQLException ex) {
            ex.printStackTrace();

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudieron cargar los productos",
                    ex.getMessage()
            );
        }
    }

    @FXML
    private void buscarFiltro() {
        /*
         * La consulta inicia con los INNER JOIN necesarios
         * y con una condición permanente:
         *
         * p.estado = 1
         *
         * De esta forma nunca mostramos productos
         * eliminados lógicamente.
         */
        // StringBuilder permite ir agregando condiciones a la consulta base.
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.id_productos,                        -- id del producto
                    p.codigo,                              -- código
                    p.nombre,                              -- nombre
                    p.descripcion,                         -- descripción
                    p.precio,                              -- precio
                    p.stock,                               -- existencias
                    p.fecha_creacion,                      -- fecha de registro
                    p.id_categoria,                        -- id de la categoría
                    c.nombre AS categoria,                 -- nombre de la categoría
                    m.id_marca,                            -- id de la marca
                    m.nombre AS marca,                     -- nombre de la marca
                    p.estado                               -- 1 = activo
                FROM catalogos.productos p                 -- tabla principal (alias p)
                INNER JOIN catalogos.categorias c          -- une con categorías (alias c)
                    ON p.id_categoria = c.id_categorias    -- relación producto-categoría
                INNER JOIN catalogos.marcas m              -- une con marcas (alias m)
                    ON p.id_marca = m.id_marca             -- relación producto-marca
                WHERE p.estado = 1                         -- solo productos activos
                """);



        /*
         * Aquí almacenaremos los valores que posteriormente
         * serán enviados al PreparedStatement.
         */
        List<Object> parametros =
                new ArrayList<>();
        // Texto del campo de búsqueda sin espacios en los extremos.
        String texto =
                txtBuscar.getText().trim();



        // Solo se filtra por texto si el usuario escribió algo.
        if (!texto.isBlank()) {
            /*
             * OR permite que el texto coincida con código
             * o nombre.
             *
             * ILIKE permite búsquedas parciales sin importar
             * mayúsculas y minúsculas.
             */
            sql.append("""

                    AND (
                        p.codigo ILIKE ?      -- coincidencia parcial en el código
                        OR p.nombre ILIKE ?   -- o coincidencia parcial en el nombre
                    )
                    """);



            // Valor del primer ?: el texto entre % significa "contiene".
            parametros.add(
                    "%" + texto + "%"
            );
            // Valor del segundo ?: el mismo texto para el nombre.
            parametros.add(
                    "%" + texto + "%"
            );
        }
        // Categoría elegida en el filtro (null si no eligió ninguna).
        Categoria categoria =
                cmbCategoriaFiltro.getValueOf();



        // Solo se filtra por categoría si se eligió una.
        if (categoria != null) {
            sql.append("""

                    AND p.id_categoria = ?    -- productos de esa categoría
                    """);
            // Valor del ?: id de la categoría elegida.
            parametros.add(
                    categoria.getId_categoria()
            );
        }
        // Fecha inicial del filtro (null si está vacía).
        LocalDate fechaDesde =
                dtpDesde.getValue();



        // Solo se filtra si hay fecha inicial.
        if (fechaDesde != null) {
            sql.append("""

                    AND p.fecha_creacion >= ?    -- creados desde esa fecha
                    """);
            parametros.add(fechaDesde); // Valor del ?: fecha inicial.
        }
        // Fecha final del filtro (null si está vacía).
        LocalDate fechaHasta =
                dtpHasta.getValue();



        // Solo se filtra si hay fecha final.
        if (fechaHasta != null) {
            sql.append("""

                    AND p.fecha_creacion <= ?    -- creados hasta esa fecha
                    """);
            parametros.add(fechaHasta); // Valor del ?: fecha final.
        }



        /*
         * Verificamos que las fechas tengan sentido.
         *
         * "Desde" no debería ser posterior a "Hasta".
         */
        if (
                fechaDesde != null
                        &&
                        fechaHasta != null
                        &&
                        fechaDesde.isAfter(fechaHasta)
        ) {
            // Avisa que el rango de fechas está al revés.
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Rango de fechas incorrecto",
                    "La fecha inicial no puede ser posterior a la fecha final."
            );
            return; // Cancela la búsqueda.
        }
        // Precio mínimo; queda null si el campo está vacío.
        BigDecimal precioMinimo = null;



        // Solo se filtra si escribió un precio mínimo.
        if (!txtPrecioMinimo.getText().isBlank()) {
            try {
                // Convierte el texto a número.
                precioMinimo =
                        new BigDecimal(
                                txtPrecioMinimo
                                        .getText()
                                        .trim()
                        );



                // El precio mínimo no puede ser negativo.
                if (
                        precioMinimo.compareTo(
                                BigDecimal.ZERO
                        ) < 0
                ) {
                    mostrarAlerta(
                            Alert.AlertType.WARNING,
                            "Precio incorrecto",
                            "El precio mínimo no puede ser negativo."
                    );
                    return; // Cancela la búsqueda.
                }



                sql.append("""

                        AND p.precio >= ?    -- precio mayor o igual al mínimo
                        """);
                parametros.add(precioMinimo); // Valor del ?: precio mínimo.
            } catch (NumberFormatException e) { // El texto no era un número.
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Precio incorrecto",
                        "El precio mínimo debe ser numérico."
                );
                txtPrecioMinimo.requestFocus(); // Lleva el cursor al precio mínimo.
                return;                      // Cancela la búsqueda.
            }
        }
        // Precio máximo; queda null si el campo está vacío.
        BigDecimal precioMaximo = null;



        // Solo se filtra si escribió un precio máximo.
        if (!txtPrecioMaximo.getText().isBlank()) {
            try {
                // Convierte el texto a número.
                precioMaximo =
                        new BigDecimal(
                                txtPrecioMaximo
                                        .getText()
                                        .trim()
                        );



                // El precio máximo no puede ser negativo.
                if (
                        precioMaximo.compareTo(
                                BigDecimal.ZERO
                        ) < 0
                ) {
                    mostrarAlerta(
                            Alert.AlertType.WARNING,
                            "Precio incorrecto",
                            "El precio máximo debe ser numérico.",
                            "Ingresa un número válido para realizar la búsqueda."
                    );
                    return; // Cancela la búsqueda.
                }



                sql.append("""

                        AND p.precio <= ?    -- precio menor o igual al máximo
                        """);
                parametros.add(precioMaximo); // Valor del ?: precio máximo.
            } catch (NumberFormatException e) { // El texto no era un número.
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Precio incorrecto",
                        "El precio máximo debe ser numérico.",
                        "Ingresa un número válido para realizar la búsqueda."
                );
                txtPrecioMaximo.requestFocus(); // Lleva el cursor al precio máximo.
                return;                      // Cancela la búsqueda.
            }
        }



        /*
         * Validamos que el precio mínimo no sea superior
         * al precio máximo.
         */
        if (
                precioMinimo != null
                        &&
                        precioMaximo != null
                        &&
                        precioMinimo.compareTo(
                                precioMaximo
                        ) > 0
        ) {
            // Avisa que el rango de precios está al revés.
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Rango de precios incorrecto",
                    "El precio mínimo no puede ser mayor que el precio máximo.",
                    "Por favor, corrige los valores ingresados para poder filtrar."
            );
            return; // Cancela la búsqueda.
        }




        // Si marcó la casilla, solo se muestran productos con stock.
        if (chkProductosExistentes.isSelected()) {
            sql.append("""

                    AND p.stock > 0    -- solo con existencias
                    """);
        }




        // Criterio de orden elegido en el ComboBox.
        String orden =
                cmbOrdenarFiltro.getValue();



        // Si no eligió ninguno, se ordena por nombre.
        if (orden == null) {
            orden = "Nombre";
        }



        // Agrega el ORDER BY según la opción elegida.
        switch (orden) {
            case "Precio menor a mayor" ->
                    sql.append("""

                            ORDER BY p.precio ASC    -- más barato primero
                            """);
            case "Precio mayor a menor" ->
                    sql.append("""

                            ORDER BY p.precio DESC    -- más caro primero
                            """);
            case "Fecha más reciente" ->
                    sql.append("""

                            ORDER BY p.fecha_creacion DESC    -- más nuevo primero
                            """);
            case "Fecha más antigua" ->
                    sql.append("""

                            ORDER BY p.fecha_creacion ASC    -- más viejo primero
                            """);
            case "Stock mayor a menor" ->
                    sql.append("""

                            ORDER BY p.stock DESC    -- más existencias primero
                            """);
            case "Stock menor a mayor" ->
                    sql.append("""

                            ORDER BY p.stock ASC    -- menos existencias primero
                            """);
            default ->
                    sql.append("""

                            ORDER BY p.nombre ASC    -- orden alfabético
                            """);
        }

        // Vacía la lista solo cuando los filtros ya pasaron las validaciones.
        productos.clear();

        // try-with-resources cierra la conexión y la sentencia automáticamente.
        try (
                // Abre la conexión con PostgreSQL.
                Connection conexion =
                        ConexionBD.getConnection();
                // Precompila la consulta que se armó con el StringBuilder.
                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql.toString()
                        )
        ) {
            /*
             * Recorremos la lista de parámetros para
             * asignarlos dinámicamente al PreparedStatement.
             */
            for (
                    int i = 0;
                    i < parametros.size();
                    i++
            ) {
                // Valor que corresponde al ? número i + 1.
                Object valor =
                        parametros.get(i);



                // Las fechas se convierten a java.sql.Date.
                if (valor instanceof LocalDate fecha) {
                    ps.setDate(
                            i + 1,
                            Date.valueOf(fecha)
                    );
                    // Los precios se envían como BigDecimal.
                } else if (valor instanceof BigDecimal numero) {
                    ps.setBigDecimal(
                            i + 1,
                            numero
                    );
                    // Textos y enteros se envían tal cual.
                } else {
                    ps.setObject(
                            i + 1,
                            valor
                    );
                }
            }



            // Ejecuta la consulta y recorre cada fila del resultado.
            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {
                while (rs.next()) {
                    // Convierte la fila en un Producto y lo agrega a la lista.
                    productos.add(
                            crearProductoDesdeResultSet(rs)
                    );
                }
            }





            // Muestra la lista filtrada en la tabla.
            tblProducto.setItems(
                    productos
            );
            // Si ningún producto cumple los filtros, se avisa.
            if (productos.isEmpty()) {
                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Sin resultados",
                        "No existen productos que coincidan con los filtros aplicados.",
                        "Intenta ajustar o limpiar los criterios de búsqueda para ver más registros."
                );
            }
        } catch (SQLException e) { // Captura errores de la base de datos.
            // Muestra el detalle técnico del error.
            mostrarErrorBaseDatos(
                    "No se pudo realizar la búsqueda.",
                    e
            );
        }
    }

    private void cargarOpcionesOrden() {

        cmbOrdenarFiltro.setItems(
                FXCollections.observableArrayList(
                        "Nombre",
                        "Precio menor a mayor",
                        "Precio mayor a menor",
                        "Fecha más reciente",
                        "Fecha más antigua",
                        "Stock mayor a menor",
                        "Stock menor a mayor"
                )
        );

        cmbOrdenarFiltro.setValue("Nombre");
    }

    private Producto crearProductoDesdeResultSet(
            ResultSet rs
    ) throws SQLException {

        return new Producto(
                rs.getInt("id_producto"),            // 1. int (id_producto)
                rs.getString("codigo"),             // 2. String (codigoProducto)
                rs.getString("nombre"),             // 3. String (nombreProducto)
                rs.getString("descripcion"),        // 4. String (descripcionProducto)
                rs.getString("precio"),             // 5. double (precioProducto)
                rs.getString("categoria"),          // 6. String (categoria)
                rs.getDouble("marca"),              // 7. String (marca)
                rs.getInt("stock"),                 // 8. int (stockProducto)
                rs.getDate("fecha_creacion"),       // 9. Date (Fecha_ingresoProducto)
                rs.getInt("id_marca"),              // 10. int (id_marca)
                rs.getInt("id_categoria"),          // 11. int (id_categoria)
                rs.getString("nombreCategoria"),    // 12. String (nombreCategoria)
                rs.getString("nombreMarca"),        // 13. String (nombreMarca)
                rs.getBoolean("estado")             // 14. boolean (estadoProducto)
        );
    }

    private String mensajeDuplicado(SQLException e) {
        if (e.getMessage() != null && e.getMessage().contains("productos_nombre_key")) {
            return "Ya existe un producto con ese nombre."; // Se repitió el nombre.
        }
        return "El código ingresado ya existe.";             // Se repitió el código.
    }

    private void cargarProductos() {

        productos.clear();

        String sql = """
            SELECT
                p.id_productos,
                p.codigo,
                p.nombre,
                p.descripcion,
                p.precio,
                p.stock,
                p.fecha_creacion,
                p.id_categoria,
                c.nombre AS categoria,
                p.id_marca,
                m.nombre AS marca,
                p.estado
            FROM catalogos.productos p
            INNER JOIN catalogos.categorias c
                ON c.id_categorias = p.id_categoria
            INNER JOIN catalogos.marcas m
                ON m.id_marca = p.id_marca
            WHERE p.estado = 1
            ORDER BY p.nombre
            """;

        try (
                Connection conexion =
                        ConexionBD.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                productos.add(
                        crearProductoDesdeResultSet(rs)
                );
            }

        } catch (SQLException e) {

            mostrarErrorBaseDatos(
                    "No se pudieron cargar los productos.",
                    e
            );
        }
    }


    private void mostrarErrorBaseDatos(String mensaje, SQLException e) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error de base de datos");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
    @FXML
    public void limpiarFormulario() {

        tblProducto.getSelectionModel().clearSelection();

        txtCodigoProd.clear();
        txtNombreProd.clear();
        txtDescripcionProd.clear();
        txtPrecioProd.clear();
        txtStockProd.clear();

        cmbCategoriaProd.setValue(null);
        cmbMarcaProd.setValue(null);
        dtpFechaProd.setValue(null);
    }


    private void cargarMarcas() {

        marcasIds.clear();

        String sql = """
            SELECT
                id_marca,
                nombre
            FROM catalogos.marcas
            WHERE estado = 1
            ORDER BY nombre
            """;

        try (
                Connection conexion =
                        ConexionBD.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                Marca marca =
                        new Marca(
                                rs.getInt("id_marca"),
                                rs.getString("nombre")
                        );

                marcasIds.add(rs.getInt("id_marca"));
            }

            cmbMarcaProd.setItems(
                    FXCollections.observableArrayList(
                            marcasIds
                    )
            );

        } catch (SQLException e) {

            mostrarErrorBaseDatos(
                    "No se pudieron cargar las marcas.",
                    e
            );
        }
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        cmbCategoriaFiltro.setValue(null);  // Quita la categoría del filtro.
        dtpDesde.setValue(null);             // Quita la fecha inicial.
        dtpHasta.setValue(null);             // Quita la fecha final.
        txtPrecioMinimo.clear();               // Borra el precio mínimo.
        txtPrecioMaximo.clear();               // Borra el precio máximo.
        chkProductosExistentes.setSelected(false); // Desmarca "solo con existencias".
        cmbOrdenarFiltro.setValue("Nombre");      // Regresa al orden por defecto.
        cargarDatos();                  // Vuelve a cargar todos los productos activos.
    }


    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String encabezado,
            String mensaje) {

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    @FXML
    private boolean validarFormulario() {

        if (txtCodigoProd.getText().isBlank() ||
                txtNombreProd.getText().isBlank() ||
                txtPrecioProd.getText().isBlank() ||
                txtStockProd.getText().isBlank() ||
                cmbCategoriaProd.getValue() == null ||
                cmbMarcaProd.getValue() == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Registro incompleto",
                    "Datos faltantes",
                    "Complete todos los campos obligatorios."
            );

            return false;
        }

        try {

            BigDecimal precio =
                    new BigDecimal(txtPrecioProd.getText().trim());

            int stock =
                    Integer.parseInt(txtStockProd.getText().trim());

            if (precio.compareTo(BigDecimal.ZERO) <= 0 ||
                    stock < 0) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Dato inválido",
                        "Precio o stock incorrecto",
                        "El precio debe ser mayor a cero y " +
                                "el stock no puede ser negativo."
                );

                return false;
            }

        } catch (NumberFormatException ex) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Dato inválido",
                    "Valores incorrectos",
                    "El precio y el stock deben ser numéricos."
            );

            return false;
        }

        if (!categoriasIds.containsKey(cmbCategoriaProd.getValue()) ||
                !marcasIds.containsKey(cmbMarcaProd.getValue())) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selección inválida",
                    "Categoría o marca incorrecta",
                    "Seleccione una categoría y marca existentes."
            );

            return false;
        }

        return true;
    }
}
