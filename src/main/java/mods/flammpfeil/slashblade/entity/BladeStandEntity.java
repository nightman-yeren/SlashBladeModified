package mods.flammpfeil.slashblade.entity;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.ItemLike;

public class BladeStandEntity extends ItemFrame implements IEntityAdditionalSpawnData {

    public Item currentType = null;
    public ItemStack currentTypeStack = ItemStack.EMPTY;

    public BladeStandEntity(EntityType<? extends BladeStandEntity> p_i50224_1_, Level p_i50224_2_) {
        super(p_i50224_1_, p_i50224_2_);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        String standTypeStr;
        if(this.currentType != null){
            standTypeStr = ForgeRegistries.ITEMS.getKey(this.currentType).toString();
        }else{
            standTypeStr = "";
        }
        compound.putString("StandType", standTypeStr);

        compound.putByte("Pose", (byte)this.getPose().ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.currentType = ForgeRegistries.ITEMS.getValue(new ResourceLocation(compound.getString("StandType")));

        this.setPose(Pose.values()[compound.getByte("Pose") % Pose.values().length]);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        this.addAdditionalSaveData(tag);
        buffer.writeNbt(tag);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        CompoundTag tag = additionalData.readNbt();
        this.readAdditionalSaveData(tag);
    }

    public static BladeStandEntity createInstanceFromPos(Level worldIn, BlockPos placePos, Direction dir, Item type) {
        BladeStandEntity e = new BladeStandEntity(SlashBlade.RegistryEvents.BladeStand, worldIn);

        e.pos = placePos;
        e.setDirection(dir);
        e.currentType = type;

        return e;
    }

    public static BladeStandEntity createInstance(PlayMessages.SpawnEntity spawnEntity, Level world) {
        return new BladeStandEntity(SlashBlade.RegistryEvents.BladeStand, world);
    }

    @Nullable
    @Override
    public ItemEntity spawnAtLocation(ItemLike iip) {
        if(iip == Items.ITEM_FRAME){
            if(this.currentType == null || this.currentType == Items.AIR)
                return null;

            iip = this.currentType;
        }
        return super.spawnAtLocation(iip);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        InteractionResult result = InteractionResult.PASS;
        if(!this.level().isClientSide && hand == InteractionHand.MAIN_HAND){
            ItemStack itemstack = player.getItemInHand(hand);
            if(player.isShiftKeyDown() && !this.getItem().isEmpty()){
                Pose current = this.getPose();
                int newIndex = (current.ordinal() + 1) % Pose.values().length;
                this.setPose(Pose.values()[newIndex]);
                result = InteractionResult.SUCCESS;
            }else if((!itemstack.isEmpty() && itemstack.getItem() instanceof ItemSlashBlade)
                        || (itemstack.isEmpty() && !this.getItem().isEmpty())){

                if(this.getItem().isEmpty()){
                    if (!this.isRemoved()) {
                        this.setItem(itemstack);
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                        this.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0F, 1.0F);
                        result = InteractionResult.SUCCESS;
                    }
                }else{
                    ItemStack displayed = this.getItem().copy();

                    this.setItem(itemstack);
                    player.setItemInHand(hand, displayed);

                    this.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
                    result = InteractionResult.SUCCESS;

                }

            }else {
                this.playSound(SoundEvents.ITEM_FRAME_ROTATE_ITEM, 1.0F, 1.0F);
                this.setRotation(this.getRotation() + 1);
                result = InteractionResult.SUCCESS;
            }
        }
        return result;
    }

    protected ItemStack getFrameItemStack() {
        return new ItemStack(currentType);
    }

}
