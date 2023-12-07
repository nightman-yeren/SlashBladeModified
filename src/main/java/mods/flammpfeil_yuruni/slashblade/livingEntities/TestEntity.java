package mods.flammpfeil_yuruni.slashblade.livingEntities;

import mods.flammpfeil_yuruni.slashblade.init.EntitiesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TestEntity extends Animal {

    public TestEntity(EntityType<TestEntity> type, Level level) {
        super(type, level);
    }

    public TestEntity(Level level, double x, double y, double z) {
        this(EntitiesInit.TEST_ENTITY.get(), level);
        setPos(x, y, z);
    }

    public TestEntity(Level level, BlockPos position) {
        this(level, position.getX(), position.getY(), position.getZ());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return new TestEntity(level, this.blockPosition());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new TemptGoal(this, 1.0D, Ingredient.of(Items.WHEAT), false));
    }

    @Override
    public boolean hurt(DamageSource p_27567_, float p_27568_) {
        return super.hurt(p_27567_, p_27568_);
    }
}
