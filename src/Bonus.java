import java.awt.*;

public class Bonus {
    //Déclaration des variables
    public int x,y,vx,vy;
    public int larg, haut;
    public int bordure;
    public Color couleurBonus;
    Rectangle hitBox;

    public Bonus (int xParam, int yParam, int largParam, int hautParam, Color coulParam, int vitx, int vity){
        x = xParam;
        y = yParam;
        larg = largParam;
        haut = hautParam;
        couleurBonus  = coulParam;
        vx=vitx;
        vy=vity;
    }



    public void dessiner(Graphics g){
        g.setColor(couleurBonus);
        g.fillOval(x,y,larg,haut);

    }

    public Rectangle getHitBox() {
        hitBox=new Rectangle(x,y,larg,haut);
        return hitBox;
    }
}
