import java.awt.*;

public class Raquette {
    //Déclaration des variables
    public int x,y;
    public int larg, haut;
    public int bordure;
    public Color couleurRaquette, couleurBord;
    Rectangle hitBox, gauche, droite;

    public Raquette (int xParam, int yParam, int largParam, int hautParam, Color coulParam){
        x = xParam;
        y = yParam;
        larg = largParam;
        haut = hautParam;
        couleurRaquette = coulParam;
    }

    public void dessiner(Graphics g){
        g.setColor(couleurRaquette);
        g.fillRect(x, y, larg, haut);
    }

    public Rectangle getHitBox() {
        hitBox=new Rectangle(x,y,larg,haut);
        return hitBox;
    }

    public Rectangle getGauche() {
        gauche=new Rectangle(x,y,larg/3,haut);
        return gauche;
    }

    public Rectangle getDroite() {
        droite=new Rectangle(x+(larg*2)/3, y, larg/3, haut);
        return droite;
    }
}
