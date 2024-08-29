package org.dementhium.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.dementhium.tools.CoordinateGrabber.GeneratedArea;
import org.dementhium.tools.CoordinateGrabber.GeneratedCoord;

/**
 * @author Stephen
 */
public class WorldMapPanel extends JPanel {

    /**
     * Eclipse.
     */
    private static final long serialVersionUID = 1L;
    BufferedImage background;
    private int seX = 0;
    private int seY = 0;
    private int nwX = 0;
    private int nwY = 0;
    Color boxColor = new Color(26, 14, 67, 136);
    private double scale = 1.0;
    private int currentX;
    private int currentY;


    public WorldMapPanel(final CoordinateGrabber frame) {
        try {
            background = ImageIO.read(new File("./data/tools/worldmap.jpg"));
            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent evt) {
                    try {
                        ((HTMLEditorKit) frame.outputPane.getEditorKit()).insertHTML((HTMLDocument) frame.outputPane.getDocument(), ((HTMLDocument) frame.outputPane.getDocument()).getLength(), "Possible X: " + xAxisPixelToGameTile(evt.getX()) + ", Possible Y: " + yAxisPixelToGameTile(evt.getY()) + ": Create spawn: <a href = \"coord:" + frame.generatedCoords.size() + "\">Yes</a> \n", 0, 0, null);
                        frame.generatedCoords.add(new GeneratedCoord(xAxisPixelToGameTile(evt.getX()), (int) ((638 + background.getHeight()) - (evt.getY() / (2 * scale)))));
                        frame.outputScroll.getVerticalScrollBar().setValue(frame.outputScroll.getVerticalScrollBar().getMaximum());
                        getMapPanel().revalidate();
                    } catch (BadLocationException ex) {
                        Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                @Override
                public void mouseEntered(MouseEvent arg0) {
                }

                @Override
                public void mouseExited(MouseEvent arg0) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    seX = e.getX();
                    seY = e.getY();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    try {
                        if (seX != 0 && seY != 0 && nwX != 0 && nwY != 0) {
                            ((HTMLEditorKit) frame.outputPane.getEditorKit()).insertHTML((HTMLDocument) frame.outputPane.getDocument(), ((HTMLDocument) frame.outputPane.getDocument()).getLength(), "Southeast X: " + xAxisPixelToGameTile(seX) + ", Southeast Y: " + yAxisPixelToGameTile(seY) + ", Northwest X: " + xAxisPixelToGameTile(nwX) + ", Northwest Y: " + yAxisPixelToGameTile(nwY) + " : Create spawn: <a href = \"area:" + frame.generatedAreas.size() + "\">Yes</a> \n", 0, 0, null);
                            frame.generatedAreas.add(new GeneratedArea( xAxisPixelToGameTile(seX), yAxisPixelToGameTile(seY), xAxisPixelToGameTile(nwX), yAxisPixelToGameTile(nwY)));
                            frame.outputScroll.getVerticalScrollBar().setValue(frame.outputScroll.getVerticalScrollBar().getMaximum());
                            nwX = 0;
                            nwY = 0;
                            seX = 0;
                            seY = 0;
                            repaint();
                        }
                    } catch (BadLocationException ex) {
                        Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (seX != 0 && seY != 0) {
                        int newX = e.getX();
                        int newY = e.getY();
                        if (newX > seX && newY < seY) {
                            nwX = e.getX();
                            nwY = e.getY();
                            repaint();
                        }
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    currentX = e.getX();
                    currentY = e.getY();
                    repaint();
                }
            });
            addMouseWheelListener(new MouseWheelListener() {

                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (e.isControlDown()) {
                        if (e.getWheelRotation() > 0 && scale != 1) {
                            scale -= .05;
                            setPreferredSize(new Dimension((int) (background.getWidth() * scale), (int) (background.getHeight() * scale))); //Couldn't find another way to update the ScrollBars, besides getPreferredSize() returns a new dimension anyway.
                            getMapPanel().revalidate();
                            repaint();
                        } else if (e.getWheelRotation() < 0 && scale != 3) {
                            scale += .05;
                            setPreferredSize(new Dimension((int) (background.getWidth() * scale), (int) (background.getHeight() * scale))); //Couldn't find another way to update the ScrollBars, besides getPreferredSize() returns a new dimension anyway.
                            getMapPanel().revalidate();
                            repaint();
                        }
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(background, AffineTransform.getScaleInstance(scale, scale), null);
        if (seX != 0 && seY != 0 && nwY != 0 && nwX != 0) {
            g2.setColor(boxColor);
            g2.fillRect(seX, nwY, nwX - seX, seY - nwY);
            g2.setColor(Color.WHITE);
            g2.drawRect(seX, nwY, nwX - seX, seY - nwY);
            g2.drawString("X: " + xAxisPixelToGameTile(nwX) + " Y: " + yAxisPixelToGameTile(nwY), nwX, nwY);
            g2.drawString("X: " + xAxisPixelToGameTile(seX) + " Y: " + yAxisPixelToGameTile(seY), seX + 1, seY + 10);
        } else {
            g2.setColor(Color.WHITE);
            g2.drawString("X: " + xAxisPixelToGameTile(currentX) + " Y: " + yAxisPixelToGameTile(currentY), (int) (currentX + 10), (int) (currentY + 29));
        }
        g2.scale(scale, scale);
    }

    public WorldMapPanel getMapPanel() {
        return this;
    }

    private int xAxisPixelToGameTile(int x) {
        return (int) ((1983 + (x / (2 * scale))));
    }

    private int yAxisPixelToGameTile(int y) {
        return (int) ((638 + background.getHeight()) - (y / (2 * scale)));
    }
}
