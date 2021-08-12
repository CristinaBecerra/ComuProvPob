package comuprovpob;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import java.awt.Color;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class ComuProvPob extends MiVentana implements DocumentListener {

    private static JTabbedPane pestañas;
    private static Connection conn;
    private static JTable tablaPueblos;
    private static JScrollPane scrollTabla;
    private static JScrollPane scrollHtml;
    private static JSplitPane panelMunic;
    private static JTextField jT_filtro;
    private static JComboBox jC_Comunidades;
    private static JComboBox jC_Provincias;
    private static JComboBox jC_provinciasEditor;

    public static void main(String[] args) {
        new ComuProvPob("Pueblos de España", 850, 650);
    }

    public ComuProvPob(String title, int ancho, int alto) {
        super(title, ancho, alto);
        conn = (new LoginBD(this, true)).getConn();

        contenidoVentana(this);
        jT_filtro.getDocument().addDocumentListener(this);
    }

    public static void contenidoVentana(MiVentana vent) {
        pestañas = new JTabbedPane();

        //Pestaña principal pueblos.......................
        pestañas.addTab("Pueblos...", panelPrincipal());

        //Pestaña resultados municipales.......................
        pestañas.addTab("Elecciones Autonómicas", panelAutonomicas());

        //Pestaña mapa pueblo.......................
        pestañas.addTab("Mapa del Pueblo", panelMapa());

        vent.add(pestañas);
        vent.setVisible(true);

    }

    //.............PESTAÑA PRINCIPAL............................................
    public static JPanel panelPrincipal() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        jC_Provincias = new JComboBox();
        jC_provinciasEditor = new JComboBox();
        scrollTabla = new JScrollPane();
        tablaPueblos = cargaJTable("select * from poblaciones", scrollTabla);

        //..........Parte Superior.............................
        JPanel panelSuperior = new JPanel();

        //.....Parte Comunidades.....
        jC_Comunidades = new JComboBox();
        jC_Comunidades.addItem("Elija Comunidad...");

        actualizaComboCom(conn, jC_Comunidades);

        jC_Comunidades.addActionListener((e) -> {
            if (!(jC_Comunidades.getSelectedItem().equals("Elija Comunidad..."))) {
                actualizaComboProv(conn, jC_Comunidades, jC_Provincias);
                actualizaComboProv(conn, jC_Comunidades, jC_provinciasEditor);

                //tablaPueblos.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jC_provinciasEditor));
            } else {
                tablaPueblos = cargaJTable("select * from poblaciones", scrollTabla);
            }
        });

        //................................................
        jC_provinciasEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                int idProv;
                super.focusLost(e);
                int rowSelec = tablaPueblos.getSelectedRow();

                if (!(jC_provinciasEditor.getSelectedIndex() == 0)) {
                    idProv = ((Provincia) jC_provinciasEditor.getSelectedItem()).getIdProvincia();
                    tablaPueblos.setValueAt(idProv, rowSelec, 1);
                }
            }
        });

        //........Parte Provincias.........
        jT_filtro = new JTextField(7);

        //ActionEvent ae = new ActionEvent(jC_Provincias, ActionEvent.ACTION_PERFORMED, "");
        //jC_Provincias.dispatchEvent(ae);
        jC_Provincias.addActionListener((e) -> {
            aplicaFiltrosPob(jT_filtro);
        });

        //.....Añadimos al panel superior.......................................
        panelSuperior.add(new JLabel("Comunidades -> "));
        panelSuperior.add(jC_Comunidades);
        panelSuperior.add(new JLabel("Provincias ->"));
        panelSuperior.add(jC_Provincias);
        panelSuperior.add(new JLabel("->Filtro por nombre:"));
        panelSuperior.add(jT_filtro);

        //.......................................................
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);

        return panelPrincipal;
    }

    //........................................................................
    private static void actualizaComboCom(Connection conn, JComboBox jC_Comunidades) {
        Statement stm;
        ResultSet rst;
        Comunidad com;

        try {
            stm = conn.createStatement();
            rst = stm.executeQuery("SELECT * FROM comunidades");

            while (rst.next()) {
                com = new Comunidad(rst.getString(1), rst.getString(2));
                jC_Comunidades.addItem(com);
            }

        } catch (SQLException ex) {
            System.out.println("Error de SQL -> " + ex.getMessage());
        }

    }

    //..........................................................................
    private static void actualizaComboProv(Connection conn, JComboBox jC_Com, JComboBox jC_Prov) {
        Statement stm;
        ResultSet rst;
        Comunidad com = (Comunidad) jC_Com.getSelectedItem();
        Provincia prov;

        try {
            jC_Prov.setModel(new DefaultComboBoxModel());
            stm = conn.createStatement();
            rst = stm.executeQuery("SELECT * FROM provincias where comunidad = '" + com.getCodigo_Com() + "'");

            while (rst.next()) {
                prov = new Provincia(rst.getString(1), rst.getInt(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getInt(6));
                jC_Prov.addItem(prov);
            }

        } catch (SQLException ex) {
            System.out.println("Error de SQL -> " + ex.getMessage());
        }

    }

    //...........Metodo que carga la tabla con los pueblos......................
    private static JTable cargaJTable(String consulta, JScrollPane scrollP) {
        DefaultTableModel dtm;
        JTable tabla = null;

        try {
            ResultSet rset = conn.createStatement().executeQuery(consulta);
            ResultSetMetaData rsetMd = rset.getMetaData();

            int numCols = rsetMd.getColumnCount();
            Object[] etiquetas = new Object[numCols];

            //Muestra los nombres de las columnas...........
            for (int i = 1; i <= numCols; i++) {
                etiquetas[i - 1] = rsetMd.getColumnLabel(i);
            }

            dtm = new DefaultTableModel(etiquetas, 0);

            //Añadimos los datos de los pueblos.............
            while (rset.next()) {
                for (int i = 1; i <= numCols; i++) {
                    etiquetas[i - 1] = rset.getObject(i);
                }
                dtm.addRow(etiquetas);
            }

            tabla = new JTable(dtm);

            tabla.setRowSorter(new TableRowSorter<>(dtm));
            tabla.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jC_provinciasEditor) {

                /*@Override
                public Object getCellEditorValue() {
                    return ((Provincia) jC_provinciasEditor.getSelectedItem()).getIdProvincia();
                }*/

            });

            scrollP.setViewportView(tabla);

        } catch (SQLException ex) {
            System.out.println("ERROR DE SQL ->" + ex.getMessage());
        }

        return tabla;
    }

    //..................PESTAÑA ELECCIONES......................................
    private static JSplitPane panelAutonomicas() {
        JPanel panelSuperior = new JPanel(new GridLayout(2, 3, 10, 10));
        JLabel pueblo = new JLabel();
        JLabel idpoblacion = new JLabel();
        JLabel postal = new JLabel();
        JLabel latitud = new JLabel();
        JLabel longitud = new JLabel();

        //..............Paneles intermedios.....................................
        JPanel pobl = new JPanel();
        JPanel idPob = new JPanel();
        JPanel codPos = new JPanel();
        JPanel latPob = new JPanel();
        JPanel longPob = new JPanel();

        //...Añadimos todo a su panel intermedio y luego al GridLayout..........
        pobl.add(new JLabel("~Población:"));
        pueblo.setFont(pueblo.getFont().deriveFont(18f));
        pueblo.setBorder(new LineBorder(Color.orange));
        pobl.add(pueblo);
        panelSuperior.add(pobl);

        idPob.add(new JLabel("~Id Población:"));
        idPob.add(idpoblacion);
        panelSuperior.add(idPob);

        codPos.add(new JLabel("~Cod. Postal:"));
        codPos.add(postal);
        panelSuperior.add(codPos);

        latPob.add(new JLabel("~Latitud:"));
        latPob.add(latitud);
        panelSuperior.add(latPob);

        longPob.add(new JLabel("~Longitud:"));
        longPob.add(longitud);
        panelSuperior.add(longPob);

        //.........Panel Inferior...............................................
        JPanel panelInferior = new JPanel();
        JEditorPane panelHtml = new JEditorPane();

        String xml = "src\\datos\\xml\\ResultadoAutonomicas.xml";
        String xsl = "src\\datos\\xsl\\municipalesLM3.xsl";

        //.......Mouse Listener sobre la tabla..................................
        tablaPueblos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int rowSelec = tablaPueblos.getSelectedRow();
                actualizaPuebloMunic(pueblo, idpoblacion, postal, latitud, longitud, rowSelec);
                String puebloS = tablaPueblos.getValueAt(rowSelec, 2).toString();

                transformaXML(xml, xsl, puebloS, panelHtml);

            }

        });

        panelInferior.add(panelHtml);
        scrollHtml = new JScrollPane(panelInferior);
        panelMunic = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelSuperior, scrollHtml);

        return panelMunic;
    }

    //.................PESTAÑA MAPA............................................. 
    private static JPanel panelMapa() {
        JPanel panelMapa = new JPanel();
        return panelMapa;
    }

    //..........................................................................
    private static void aplicaFiltrosPob(JTextField jT_nombre) {
        TableRowSorter trs = (TableRowSorter) tablaPueblos.getRowSorter();

        if (jC_Comunidades.getSelectedIndex() == 0) {
            trs.setRowFilter(RowFilter.regexFilter(jT_nombre.getText(), 2));

        } else {
            int prov = ((Provincia) jC_Provincias.getSelectedItem()).getIdProvincia();
      
            RowFilter filtroProv = RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, prov, 1);
            RowFilter filtroNombrePueb = RowFilter.regexFilter(jT_nombre.getText(), 2);

            LinkedList lista = new LinkedList();
            lista.add(filtroProv);
            lista.add(filtroNombrePueb);
            
            
            RowFilter filtroAnd = RowFilter.andFilter(lista);
            trs.setRowFilter(filtroAnd);
        }

        tablaPueblos.setRowSorter(trs);
    }

    //...................DOCUMENT LISTENER......................................
    @Override
    public void insertUpdate(DocumentEvent e) {
        aplicaFiltrosPob(jT_filtro);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        aplicaFiltrosPob(jT_filtro);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    //............Genera un html según el pueblo seleccionado...................
    public static void transformaXML(String xml, String xsl, String param, JEditorPane panelEditor) {
        String html;

        html = "src/datos/html/" + param + ".html";

        File paginaHTML = new File(html);
        String urlDocumento = "file://localhost/" + paginaHTML.getAbsolutePath();

        if (!(new File(html)).exists()) {
            transforma(xml, xsl, html, param);
        }
        try {
            panelEditor.setPage(urlDocumento);

        } catch (IOException ex) {
            System.out.println("Error IO-> " + ex.getMessage());
        }

    }

    //............Metodo que aplica XSl al XML y devuelve un html...............
    public static void transforma(String xml, String xsl, String html, String param) {
        try {
            TransformerFactory transFact = new TransformerFactoryImpl();

            Source xslDoc = new StreamSource(xsl);
            Source xmlDoc = new StreamSource(xml);

            //String outputFileName = html;
            OutputStream htmlFile = new FileOutputStream(html);

            Transformer transformer = transFact.newTransformer(xslDoc);

            if (param != "-1") {
                transformer.setParameter("ciudad", param);
            }
            transformer.transform(xmlDoc, new StreamResult(htmlFile));

        } catch (Exception e) {
            System.out.println("Error al aplicar XSLT:..." + e.getMessage());
        }
    }

    //........Actualiza datos pueblo seleccionado...............................
    public static void actualizaPuebloMunic(JLabel pueblo, JLabel idpoblacion, JLabel postal, JLabel latitud, JLabel longitud, int rowSelec) {
        pueblo.setText(tablaPueblos.getValueAt(rowSelec, 2).toString());
        idpoblacion.setText(tablaPueblos.getValueAt(rowSelec, 0).toString());
        postal.setText(tablaPueblos.getValueAt(rowSelec, 4).toString());
        latitud.setText(tablaPueblos.getValueAt(rowSelec, 5).toString());
        longitud.setText(tablaPueblos.getValueAt(rowSelec, 6).toString());
    }

}
