import java.awt.*;

public class Vie {
   //Déclaration des variables
    public int x,y,vx,vy;
    public int larg, haut;
    public Color couleurVie;

    public Vie (int xParam, int yParam, int largParam, int hautParam, Color coulParam){
        x = xParam;
        y = yParam;
        larg = largParam;
        haut = hautParam;
        couleurVie  = coulParam;

    }
    public void dessiner(Graphics g){
        g.setColor(couleurVie);
        g.fillOval(x,y,larg,haut);
    }
}
