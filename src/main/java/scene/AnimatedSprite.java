package scene;

import core.MainLoop;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AnimatedSprite extends SpriteBase {
    private final HashMap<String, SpriteFrames> animations = new HashMap<>();
    boolean playing = false;
    boolean playBackwards = false;
    String currentAnimation = null;
    int currentAnimationFrame = 0;
    private long frameTime = 0;
    private double speedScale = 1.0;

    public AnimatedSprite() {
        super();
    }

    public boolean hasAnimation(String id) {
        return animations.containsKey(id);
    }
    public SpriteFrames animation(String id) {
        return animations.get(id);
    }
    public boolean isPlaying() {
        return playing;
    }
    public String currentAnimation() {
        return currentAnimation;
    }
    public int currentFrame() {
        return currentAnimationFrame;
    }
    public double speedScale() {
        return speedScale;
    }

    public SpriteFrames addAnimation(String id) {
        SpriteFrames a = new SpriteFrames(id, this);
        animations.put(id, a);
        return a;
    }
    public void setSpeedScale(double speedScale) {
        this.speedScale = speedScale;
    }

    public void play(String id) {
        play(id, false);
    }
    public void play(String id, boolean backwards) {
        if(hasAnimation(id)) {
            if(isPlaying()) {
                stop();
            }
            if(!id.equals(currentAnimation)) {
                currentAnimation = id;
                if(backwards) {
                    currentAnimationFrame = animation(id).frameCount() - 1;
                } else {
                    currentAnimationFrame = 0;
                }
            }
            frameTime = 0;
            playBackwards = backwards;
            playing = true;
        }
    }
    public void stop() {
        playing = false;
    }
    public void reset() {
        currentAnimation = null;
        currentAnimationFrame = 0;
        frameTime = 0;
        playing = false;
    }

    @Override
    protected void update() {
        super.update();

        if(!isPlaying()) {
            return;
        }
        SpriteFrames anim = animation(currentAnimation);
        if(anim == null) {
            return;
        }

        frameTime += MainLoop.NANOS_PER_TICK;

        if(frameTime > MainLoop.NANOSECOND / (anim.speed() * speedScale)) {
            if(playBackwards) {
                currentAnimationFrame--;
            } else {
                currentAnimationFrame++;
            }
            boolean looped = false;
            if(currentAnimationFrame >= anim.frameCount()) {
                currentAnimationFrame = 0;
                looped = true;
            } else if(currentAnimationFrame < 0) {
                currentAnimationFrame = anim.frameCount() - 1;
                looped = true;
            }
            if(looped && !anim.isLoop()) {
                stop();
            }
            frameTime = 0;
        }
    }

    @Override
    protected void render(Graphics2D g) {
        super.render(g);

        if(currentAnimation == null) {
            return;
        }
        SpriteFrames anim = animation(currentAnimation);
        if(anim == null || currentAnimationFrame < 0 || currentAnimationFrame >= anim.frameCount()) {
            return;
        }
        BufferedImage image = anim.getFrame(currentAnimationFrame);

        g.translate(-size().width / 2.0, -size().height / 2.0);
        g.scale(size().width / (double)image.getWidth(), size().height / (double)image.getHeight());

        g.drawImage(image, 0, 0, null);
    }
}
