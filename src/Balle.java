import java.awt.*;

public class Balle {
    //Déclaration des variables
    public int x,y,vx,vy;
    public int larg, haut;
    public int bordure;
    public Color couleurBalle, couleurBord;
    Rectangle hitBox, h, b, d, g, bd, bg ;
    Image imgBalle;

    public Balle (int xParam, int yParam, int largParam, int hautParam, Color coulParam, int vitx, int vity){
        x = xParam;
        y = yParam;
        larg = largParam;
        haut = hautParam;
        couleurBalle  = coulParam;
        vx=vitx;
        vy=vity;
    }



    public void dessiner(Graphics g){
        g.setColor(couleurBalle);
        g.fillOval(x,y,larg,haut);

    }

    public Rectangle getHitBox() {
        hitBox=new Rectangle(x,y,larg,haut);
        return hitBox;
    }

    public Rectangle getH() {
        h=new Rectangle(x,y-10,larg,1);
        return h;
    }

    public Rectangle getG() {
        g=new Rectangle(x-45,y,1,haut);
        return g;
    }

    public Rectangle getB() {
        b=new Rectangle(x,y+20+10,larg,1);
        return b;
    }

    public Rectangle getD() {
        d=new Rectangle(x+20+45,y,1,haut);
        return d;
    }

    public Rectangle getBd() {
        bd=new Rectangle(x+20+45,y,1,haut);
        return bd;
    }

    public Rectangle getBg() {
        bg=new Rectangle(x-95,y,1,haut);
        return bg;
    }
}


