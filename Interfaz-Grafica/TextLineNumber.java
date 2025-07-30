import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TextLineNumber extends JComponent implements PropertyChangeListener, DocumentListener {
    private final JTextComponent component;
    private FontMetrics fontMetrics;

    public TextLineNumber(JTextComponent component) {
        this.component = component;
        setFont(component.getFont());
        component.getDocument().addDocumentListener(this);
        component.addPropertyChangeListener("font", this);
        setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY), new EmptyBorder(0, 5, 0, 5)));
        updatePreferredSize();
    }
    private void updatePreferredSize() {
        fontMetrics = component.getFontMetrics(component.getFont());
        int lineCount = component.getDocument().getDefaultRootElement().getElementCount();
        int maxDigits = Math.max(3, String.valueOf(lineCount).length());
        int width = maxDigits * fontMetrics.stringWidth("0") + getInsets().left + getInsets().right;
        if (getPreferredSize().width != width) {
            setPreferredSize(new Dimension(width, component.getHeight()));
            revalidate();
            repaint();
        }
    }
    @Override public void propertyChange(PropertyChangeEvent evt) {
        if ("font".equals(evt.getPropertyName())) {
            setFont((Font) evt.getNewValue());
            updatePreferredSize();
        }
    }
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        Rectangle clip = g.getClipBounds();
        int startLine = getLineAtPoint(clip.y);
        int endLine = getLineAtPoint(clip.y + clip.height);
        for (int line = startLine; line <= endLine; line++) {
            if (line < 0) continue;
            String text = String.valueOf(line + 1);
            try {
                int y = getLineY(line);
                g.drawString(text, getInsets().left, y);
            } catch (BadLocationException e) {}
        }
    }
    private int getLineY(int line) throws BadLocationException {
        return component.modelToView(component.getDocument().getDefaultRootElement().getElement(line).getStartOffset()).y + fontMetrics.getAscent();
    }
    private int getLineAtPoint(int y) {
        Element root = component.getDocument().getDefaultRootElement();
        int pos = component.viewToModel(new Point(0, y));
        return root.getElementIndex(pos);
    }
    @Override public void insertUpdate(DocumentEvent e) { updatePreferredSize(); repaint(); }
    @Override public void removeUpdate(DocumentEvent e) { updatePreferredSize(); repaint(); }
    @Override public void changedUpdate(DocumentEvent e) { updatePreferredSize(); repaint(); }
}