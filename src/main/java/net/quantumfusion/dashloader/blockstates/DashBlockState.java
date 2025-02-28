package net.quantumfusion.dashloader.blockstates;

import com.google.common.collect.ImmutableMap;
import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import io.activej.serializer.annotations.SerializeNullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.registry.Registry;
import net.quantumfusion.dashloader.DashRegistry;
import net.quantumfusion.dashloader.mixin.StateAccessor;
import net.quantumfusion.dashloader.util.Dashable;
import net.quantumfusion.dashloader.util.PairMap;
import org.apache.commons.lang3.tuple.Pair;

public class DashBlockState implements Dashable {

    @Serialize(order = 0)
    public final Long owner;

    @Serialize(order = 1)
    @SerializeNullable()
    public final PairMap<Long, Long> entriesEncoded;


    public DashBlockState(@Deserialize("owner") Long owner,
                          @Deserialize("entriesEncoded") PairMap<Long, Long> entriesEncoded) {
        this.owner = owner;
        this.entriesEncoded = entriesEncoded;
    }

    public DashBlockState(BlockState blockState, DashRegistry registry) {
        StateAccessor<Block, BlockState> accessState = ((StateAccessor<Block, BlockState>) blockState);
        entriesEncoded = new PairMap<>();
        accessState.getEntries().forEach((property, comparable) -> {
            final Pair<Long, Long> propertyPointer = registry.createPropertyPointer(property, comparable);
            entriesEncoded.put(propertyPointer.getLeft(), propertyPointer.getRight());
        });
        owner = registry.createIdentifierPointer(Registry.BLOCK.getId(blockState.getBlock()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final BlockState toUndash(final DashRegistry registry) {
        final ImmutableMap.Builder<Property<?>, Comparable<?>> builder = ImmutableMap.builder();
        entriesEncoded.forEach((propPntr, valuePntr) -> builder.put(registry.getProperty(propPntr, valuePntr)));
        return new BlockState(Registry.BLOCK.get(registry.getIdentifier(owner)), builder.build(), null);
    }

}
