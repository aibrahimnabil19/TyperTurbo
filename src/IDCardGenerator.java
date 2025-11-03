import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IDCardGenerator extends JFrame {
    // --- Model for one text‐field’s position on the card
    static class FieldPosition {
        final String label;
        final int x, y;
        final Font font;
        FieldPosition(String label, int x, int y, Font font) {
            this.label=label; this.x=x; this.y=y; this.font=font;
        }
    }

    // Measured positions on a 1004×650px template:
    private static final FieldPosition[] FIELDS = {
            new FieldPosition("Nom",        200, 240, new Font("SansSerif", Font.BOLD, 26)),
            new FieldPosition("Prénoms",    200, 280, new Font("SansSerif", Font.BOLD, 26)),
            new FieldPosition("Né(e) le",   200, 320, new Font("SansSerif", Font.BOLD, 26)),
            new FieldPosition("Niveau",     200, 360, new Font("SansSerif", Font.BOLD, 26)),
            new FieldPosition("Filière",    200, 400, new Font("SansSerif", Font.BOLD, 26)),
            new FieldPosition("Matricule",  200, 440, new Font("SansSerif", Font.BOLD, 26)),
    };

    private BufferedImage template;
    private BufferedImage photo;
    private final JTextField[] textFields = new JTextField[FIELDS.length];
    private final JLabel preview = new JLabel();

    public IDCardGenerator() {
        super("Générateur de Carte Étudiant");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // ——— Top: load template + preview ———
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton loadTpl = new JButton("Charger le modèle (TIFF)");
        loadTpl.addActionListener(e->loadTemplate());
        top.add(loadTpl);
        top.add(preview);
        add(top, BorderLayout.NORTH);

        // ——— Center: form fields + photo ———
        JPanel form = new JPanel(new GridLayout(FIELDS.length+1, 2, 5,5));
        for (int i=0; i<FIELDS.length; i++) {
            form.add(new JLabel(FIELDS[i].label + " :"));
            textFields[i] = new JTextField();
            form.add(textFields[i]);
        }
        JButton loadPhoto = new JButton("Charger Photo");
        loadPhoto.addActionListener(e->loadPhoto());
        form.add(loadPhoto);
        form.add(new JLabel());  // filler
        add(form, BorderLayout.CENTER);

        // ——— Bottom: generate & save ———
        JButton gen = new JButton("Générer et Enregistrer");
        gen.addActionListener(e->generateAndSave());
        add(gen, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadTemplate() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            try {
                template = ImageIO.read(chooser.getSelectedFile());
                // scale down for preview
                Image thumb = template.getScaledInstance(300, -1, Image.SCALE_SMOOTH);
                preview.setIcon(new ImageIcon(thumb));
                pack();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lecture TIFF : " + ex);
            }
        }
    }

    private void loadPhoto() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            try {
                photo = ImageIO.read(chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Photo chargée !");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lecture image : " + ex);
            }
        }
    }

    private void generateAndSave() {
        if (template == null) {
            JOptionPane.showMessageDialog(this, "Veuillez charger d’abord le modèle !");
            return;
        }

        // Create a fresh copy to draw on
        BufferedImage card = new BufferedImage(
                template.getWidth(), template.getHeight(), BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = card.createGraphics();
        g.drawImage(template, 0, 0, null);

        // Draw each text field
        for (int i=0; i<FIELDS.length; i++) {
            FieldPosition fp = FIELDS[i];
            g.setFont(fp.font);
            g.drawString(textFields[i].getText(), fp.x, fp.y);
        }

        // Draw photo inside a circle at approx. (x=700,y=250,w=260,h=260)
        if (photo != null) {
            int px=700, py=250, pw=260, ph=260;
            // clip to circle
            Shape circle = new java.awt.geom.Ellipse2D.Float(px, py, pw, ph);
            g.setClip(circle);
            g.drawImage(photo, px, py, pw, ph, null);
            g.setClip(null);
            // optional: draw circle border
            g.setStroke(new BasicStroke(4));
            g.draw(circle);
        }

        g.dispose();

        // Save
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("C:\\Users\\User\\Documents\\untitled\\src\\Images\\001.tif"));
        if (chooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
            try {
                ImageIO.write(card, "TIFF", chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Carte créée avec succès !");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur d’écriture : " + ex);
            }
        }
    }

    public static void main(String[] args) {
        // Make sure you have TwelveMonkeys ImageIO on your classpath for TIFF support
        SwingUtilities.invokeLater(IDCardGenerator::new);
    }
}
