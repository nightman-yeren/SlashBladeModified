package mods.flammpfeil_yuruni.slashblade.init;

import mods.flammpfeil_yuruni.slashblade.SlashBlade;
import mods.flammpfeil_yuruni.slashblade.livingEntities.TestEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntitiesInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SlashBlade.modid);

    public static final RegistryObject<EntityType<TestEntity>> TEST_ENTITY = ENTITIES.register("test_entity",
            () -> EntityType.Builder.<TestEntity>of(TestEntity::new, MobCategory.CREATURE)
                    .sized(1.0f, 1.0f)
                    .build(new ResourceLocation(SlashBlade.modid, "test_entity").toString())
    );

}
