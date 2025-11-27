package javatro.vista.paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javatro.modelo.tienda.ItemTienda;
import javatro.vista.componentes.BotonBalatro;

public class PanelCartaTienda extends JPanel {

    private ItemTienda item;
    private BotonBalatro btnComprar;

    public PanelCartaTienda(ItemTienda item, ActionListener alComprar) {
        this.item = item;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(120, 200)); 

        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(100, 140));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            URL url = getClass().getResource("/javatro/recursos/imagenes" + item.getRutaImagen());
            if(url != null) {
                ImageIcon icon = new ImageIcon(javax.imageio.ImageIO.read(url));
                Image img = icon.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(img));
            } else {
                lblImagen.setText(item.getNombre());
                lblImagen.setForeground(Color.WHITE);
            }
        } catch(Exception e) {}

        lblImagen.setToolTipText("<html><b>" + item.getNombre() + "</b><br>" + item.getDescripcion() + "</html>");

        btnComprar = new BotonBalatro("$" + item.getPrecio(), new Color(0, 180, 80));
        btnComprar.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnComprar.setPreferredSize(new Dimension(100, 35));
        btnComprar.addActionListener(alComprar);

        this.add(lblImagen, BorderLayout.CENTER);
        this.add(btnComprar, BorderLayout.SOUTH);
    }

    public void deshabilitarCompra() {
        btnComprar.setEnabled(false);
        btnComprar.setText("VENDIDO");
    }
}