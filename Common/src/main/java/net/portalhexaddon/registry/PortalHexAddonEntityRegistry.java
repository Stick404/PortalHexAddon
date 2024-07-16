package net.portalhexaddon.registry;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import net.portalhexaddon.entites.HexPortalEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static at.petrak.hexcasting.api.HexAPI.modLoc;


public class PortalHexAddonEntityRegistry {


    public static final EntityType<HexPortalEntity> HEXPORTALENTIY = register(
            "hexportalentity",
            EntityType.Builder.of((EntityType.EntityFactory<HexPortalEntity>) HexPortalEntity::new, MobCategory.MISC)

    );



    public static void registerEntities (BiConsumer<EntityType<?>, ResourceLocation> r) {
        for (var e : ENTITIES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, EntityType<?>> ENTITIES = new LinkedHashMap<>();

    private static <T extends Entity> EntityType<T> register (String id, EntityType<T> type) {
        var old = ENTITIES.put(modLoc(id), type);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + id);
        }
        return type;

    }
}
