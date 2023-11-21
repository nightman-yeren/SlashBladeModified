package mods.flammpfeil.slashblade.optional.playerAnim;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import jp.nyatla.nymmd.MmdException;
import jp.nyatla.nymmd.MmdMotionPlayerGL2;
import jp.nyatla.nymmd.MmdPmdModelMc;
import jp.nyatla.nymmd.MmdVmdMotionMc;
import jp.nyatla.nymmd.core.PmdBone;
import jp.nyatla.nymmd.types.MmdVector3;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.client.renderer.model.BladeMotionManager;
import mods.flammpfeil.slashblade.util.TimeValueHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Math;
import java.util.List;
import java.util.Map;

public class VmdAnimation implements IAnimation {
     static final LazyOptional<MmdPmdModelMc> alex =
            LazyOptional.of(() -> {
                try {
                    return new MmdPmdModelMc(new ResourceLocation(SlashBlade.modid, "model/pa/alex.pmd"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MmdException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            });

    static final LazyOptional<MmdMotionPlayerGL2> motionPlayer =
            LazyOptional.of(() -> {
                MmdMotionPlayerGL2 mmp = new MmdMotionPlayerGL2();;

                alex.ifPresent(pmd -> {
                    try {
                        mmp.setPmd(pmd);
                    } catch (MmdException e) {
                        e.printStackTrace();
                    }
                });

                return mmp;
            });

    int currentTick;

    final ResourceLocation loc;
    double start;
    double end;
    double span;
    boolean loop;

    private boolean isRunning = true;

    private boolean blendArms = false;
    private boolean blendLegs = true;

    public VmdAnimation(ResourceLocation loc, double start, double end, boolean loop){
        this.loc = loc;
        this.start = start;
        this.end = end;

        this.span = TimeValueHelper.getTicksFromFrames((float)Math.abs(end - start));;

        this.loop = loop;

        currentTick = 0;
    }

    public VmdAnimation setBlendArms(boolean blend){
        blendArms = blend;
        return this;
    }
    public VmdAnimation setBlendLegs(boolean blend){
        blendLegs = blend;
        return this;
    }

    @Override
    public void tick() {
        if (this.isRunning) {
            this.currentTick++;

            double endTicks = span;
            this.loop = false;
            if (this.loop && endTicks < this.currentTick) {
                this.currentTick = 0;
            }

            if (endTicks <= currentTick) {
                this.stop();
            }
        }
    }

    public void play(){
        this.currentTick = 0;
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean isActive() {
        return this.isRunning;
    }
    Map<String,String> nameMap = initNamemap();

    private Map<String, String> initNamemap() {
        Map<String,String> map = Maps.newHashMap();
        map.put("leftArm","left arm");
        map.put("rightArm","right arm");
        map.put("leftLeg","left leg");
        map.put("rightLeg","right leg");
        return map;
    }

    List<String> arms = Lists.newArrayList("leftArm","rightArm");
    List<String> legs = Lists.newArrayList("leftLeg","rightLeg");


    @Override
    public @NotNull Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        //this.setupAnim(tickDelta);

        double motionScale = 1.0 / 16.0;
        float bodyScale = (float)motionScale;
        float finalizeScale = 2.0f;

        Vector3f blend = new Vector3f(value0.getX(),value0.getY(),value0.getZ());

        if(type != TransformType.POSITION && (
                (!this.blendArms && arms.contains(modelName))
                || (!this.blendLegs && legs.contains(modelName))
                )){
            blend.mul(0);
        }

        if(!motionPlayer.isPresent()) return value0;
        MmdMotionPlayerGL2 mmp = motionPlayer.orElse(null);

        String boneName = modelName;
        if(nameMap.containsKey(modelName)){
            boneName = nameMap.get(modelName);
        }

        PmdBone bone = mmp.getBoneByName(boneName);

        if(bone != null){
            switch (type){
                case POSITION : {
                    MmdVector3 org = bone.m_vec3Position;
                    Vector3f tmp = new Vector3f(org.x,org.y,org.z);
                    if (modelName.equals("body")) {
                        tmp = tmp.mul(bodyScale);
                    } else {
                        tmp = tmp.mul(1, -1, 1);
                    }

                    tmp.mul(finalizeScale).add(blend);
                    return new Vec3f(tmp.x,tmp.y,tmp.z);
                }
                case ROTATION : {
                    Quaterniond qt = new Quaterniond(bone.m_vec4Rotate.x, bone.m_vec4Rotate.y, bone.m_vec4Rotate.z, bone.m_vec4Rotate.w);
                    Vector3d tmp = QuaternionToEulerZYX(qt);

                    if (modelName.equals("body")) {
                        tmp = tmp.mul(1, -1, -1);
                    } else {
                        tmp = tmp.mul(-1, 1, -1);
                    }

                    tmp.add(blend);
                    return new Vec3f((float) tmp.x, (float) tmp.y, (float) tmp.z);
                }
            }
        }
        /**/

        /*
        int idx = mmp.getBoneIndexByName(boneName);
        if (0 <= idx) {
            float[] buf = new float[16];
            mmp._skinning_mat[idx].getValue(buf);

            Matrix4f mat = VectorHelper.matrix4fFromArray(buf);
            mat = (new Matrix4f()).scale(1, -1, 1).mul(mat).scale(1,-1,1).scale((float)scale);
            //mat.transpose();

            switch (type){
                case POSITION -> {
                    Vector3f tmp = new Vector3f();
                    mat.getTranslation(tmp);
                    MmdVector3 vec = bone._pmd_bone_position;

                    return new Vec3f(tmp.x,tmp.y,tmp.z).add(value0);
                }
                case ROTATION -> {

                    Quaternionf qt = new Quaternionf(bone.m_vec4Rotate.x,bone.m_vec4Rotate.y,bone.m_vec4Rotate.z,bone.m_vec4Rotate.w);
                    Vector3f tmp = new Vector3f();
                    qt.getEulerAnglesXYZ(tmp);

                    return new Vec3f(tmp.x,tmp.y,tmp.z);

                    //Vector3f tmp = new Vector3f();
                    //mat = mat;
                    //mat.getEulerAnglesZYX(tmp);
                    //return new Vec3f(tmp.x,tmp.y,tmp.z);

                }
            }
        }
        /**/

        return value0;
    }

    Vector3d QuaternionToEulerZYX(Quaterniond qt){
        Vector3d tmp = new Vector3d();

        double a_x_x = Math.pow(qt.w, 2) + Math.pow(qt.x, 2) - Math.pow(qt.y, 2) - Math.pow(qt.z, 2);
        double a_x_y = 2 * (qt.x * qt.y + qt.w * qt.z);
        double a_x_z = 2 * (qt.x * qt.z - qt.w * qt.y);

        double a_y_x = 2 * (qt.x * qt.y - qt.w * qt.z);
        double a_y_y = Math.pow(qt.w, 2) - Math.pow(qt.x, 2) + Math.pow(qt.y, 2) - Math.pow(qt.z, 2);
        double a_y_z = 2 * (qt.y * qt.z + qt.w * qt.x);

        double a_z_x = 2 * (qt.x * qt.z + qt.w * qt.y);
        double a_z_y = 2 * (qt.y * qt.z - qt.w * qt.x);
        double a_z_z = Math.pow(qt.w, 2) - Math.pow(qt.x, 2) - Math.pow(qt.y, 2) + Math.pow(qt.z, 2);

        //Quaternion to Euler zyx
        tmp.z = Math.atan2(a_x_y, a_x_x);
        tmp.y = Math.asin(-a_x_z);
        tmp.x = Math.atan2(a_y_z, a_z_z);

        return tmp;
    }


    @Override
    public void setupAnim(float tickDelta) {
        if(!motionPlayer.isPresent()) return;

        MmdMotionPlayerGL2 mmp = motionPlayer.orElse(null);

        double eofTime = 0;
        MmdVmdMotionMc motion = BladeMotionManager.getInstance().getMotion(loc);
        try {
            mmp.setVmd(motion);
            eofTime = TimeValueHelper.getMSecFromFrames(motion.getMaxFrame());
        } catch (Exception e) {
            e.printStackTrace();
        }

        double time = TimeValueHelper.getMSecFromTicks((float)(currentTick + (double)tickDelta));
        time = Math.min(eofTime,time);
        time = TimeValueHelper.getMSecFromFrames((float)start) + time;

        try {
            mmp.updateMotion((float)time);
        } catch (MmdException e) {
            e.printStackTrace();
        }
    }
}
