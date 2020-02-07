import java.awt.*;

public class Briques {
    //Déclaration des variables
    public int x,y;
    public int larg, haut;
    public int bordure;
    public Color couleurBriques, couleurBord;
    Rectangle hitBox;

    public Briques (int xParam, int yParam, int largParam, int hautParam, Color coulParam, Color coulbord){
        x = xParam;
        y = yParam;
        larg = largParam;
        haut = hautParam;
        couleurBriques = coulParam;
        couleurBord = coulbord;
        hitBox = new Rectangle(x,y,larg,haut);
    }

    public void dessiner(Graphics g){
        g.setColor(couleurBriques);
        g.fillRect(x, y, larg, haut);
        g.setColor(couleurBord);
        g.drawRect(x,y,larg,haut);

    }

    public Rectangle getHitBox() {
        hitBox=new Rectangle(x,y,larg,haut);
        return hitBox;
    }

}
