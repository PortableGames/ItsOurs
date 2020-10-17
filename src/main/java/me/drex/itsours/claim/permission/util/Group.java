package me.drex.itsours.claim.permission.util;

import com.google.common.collect.Lists;
import me.drex.itsours.claim.permission.util.node.AbstractNode;
import me.drex.itsours.claim.permission.util.node.GroupNode;
import me.drex.itsours.claim.permission.util.node.SingleNode;
import net.minecraft.block.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class Group {
    public final List<AbstractNode> list;

    public Group(final List<AbstractNode> list) {
        this.list = list;
    }

    public static final Class<?>[] INTERACT_FILTER = {BlockWithEntity.class, AbstractButtonBlock.class, AnvilBlock.class, BedBlock.class, CakeBlock.class, CartographyTableBlock.class, CauldronBlock.class, ComparatorBlock.class, ComposterBlock.class, CraftingTableBlock.class, DoorBlock.class, DragonEggBlock.class, EnderChestBlock.class, FenceBlock.class, FenceGateBlock.class, FlowerPotBlock.class, GrindstoneBlock.class, JigsawBlock.class, LeverBlock.class, LoomBlock.class, NoteBlock.class, PumpkinBlock.class, RedstoneOreBlock.class, RedstoneWireBlock.class, RepeaterBlock.class, RespawnAnchorBlock.class, StairsBlock.class, StonecutterBlock.class, SweetBerryBushBlock.class, TntBlock.class, TrapdoorBlock.class};
    public static final Group BLOCK = create(Registry.BLOCK, BlockTags.getRequiredTags());

    public static final Group INTERACTABLE_BLOCK = create(Registry.BLOCK, BlockTags.getRequiredTags(), INTERACT_FILTER);

    public static final Group ENTITIES = create(Registry.ENTITY_TYPE, EntityTypeTags.getRequiredTags());

    public static final Group ITEMS = create(Registry.ITEM, ItemTags.getRequiredTags());


    public static <T> Group create(@NotNull final Registry<T> registry, @NotNull final List<? extends Tag.Identified<T>> list, Class<?>... filter) {
        Validate.notNull(registry, "Registry must not be null!");
        Validate.notNull(list, "Identified tag list must not be null!");
        final List<AbstractNode> nodes = Lists.newArrayList();

        for (final Tag.Identified<T> tag : list) {
            final List<String> entries = Lists.newArrayList();

            for (T entry : registry) {
                if (!filter(entry, filter)) continue;
                if (tag.contains(entry)) {
                    final Identifier id = registry.getId(entry);
                    Validate.notNull(id, "%s does not contain entry %s", registry.toString(), entry.toString());
                    entries.add(id.getPath());
                }
            }

            nodes.add(new GroupNode(tag.getId().getPath().toUpperCase(Locale.ENGLISH), entries.toArray(new String[0])));
        }

        for (T entry : registry) {
            if (!filter(entry, filter)) continue;
            nodes.add(new SingleNode(
                    Validate.notNull(
                            registry.getId(entry),
                            "%s does not contain entry %s", registry.toString(), entry.toString()
                    ).getPath()
            ));
        }

        return new Group(nodes);
    }

    public static <T> boolean filter(T entry, Class<?>... filter) {
        if (filter.length > 0) {
            for (Class<?> clazz : filter) {
                if (clazz.isInstance(entry)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }
}
