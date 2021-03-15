package supercoder79.ecotones.world.features.tree;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import supercoder79.ecotones.world.features.FeatureHelper;
import supercoder79.ecotones.world.features.config.OakTreeFeatureConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StraightOakTreeFeature extends Feature<OakTreeFeatureConfig> {
    public StraightOakTreeFeature(Codec<OakTreeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<OakTreeFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getPos();
        Random random = context.getRandom();
        ChunkGenerator generator = context.getGenerator();
        OakTreeFeatureConfig config = context.getConfig();

        // Ensure spawn
        if (world.getBlockState(pos.down()) != Blocks.GRASS_BLOCK.getDefaultState()) {
            return true;
        }

        world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 3);

        // TODO scale
        int height = config.getHeight() + random.nextInt(config.getRandomHeight() + 1);

        List<BlockPos> leafNodes = new ArrayList<>();

        generateTrunk(world, pos, random, height, leafNodes);

        generateLeaves(world, leafNodes);

        return true;
    }

    private void generateTrunk(StructureWorldAccess world, BlockPos start, Random random, int height, List<BlockPos> leafNodes) {
        int branchStartHeight = -1;

        for (Direction direction : FeatureHelper.HORIZONTAL) {
            int rootHeight = (int) (height * (random.nextDouble() * random.nextDouble() * 0.2 + 0.1));
            branchStartHeight = Math.max(rootHeight, branchStartHeight);

            generateRoot(world, start.offset(direction), rootHeight);
        }

        branchStartHeight++;

        for (int i = 0; i < height; i++) {
            BlockPos local = start.up(i);
            world.setBlockState(local, Blocks.OAK_LOG.getDefaultState(), 3);

            if (i == height - 1) {
                leafNodes.add(local);
            }

            int branchCount = 2 + random.nextInt(3);
            double theta = random.nextDouble() * 2 * Math.PI;

            for (int j = 0; j < branchCount; j++) {
                if (i > branchStartHeight && i < height - 3) {
                    generateBranch(world, random, local,(theta / branchCount) * i + (random.nextDouble() * 0.2), 3 + random.nextInt(3), leafNodes);
                }
            }
        }
    }

    private void generateBranch(StructureWorldAccess world, Random random, BlockPos start, double theta, int length, List<BlockPos> leafNodes) {
        if (random.nextBoolean()) {
            length++;
        }


        for (int i = 0; i < length; i++) {
            int dx = (int) (Math.cos(theta) * i);
            int dz = (int) (Math.sin(theta) * i);
            int dy = i / 2;
            BlockPos local = start.add(dx, dy, dz);

            world.setBlockState(local, Blocks.OAK_LOG.getDefaultState(), 3);

            if (i == length - 1) {
                leafNodes.add(local);
            }
        }
    }

    private void generateRoot(StructureWorldAccess world, BlockPos start, int height) {
        for (int i = 0; i < height; i++) {
            BlockPos local = start.up(i);
            world.setBlockState(local, Blocks.OAK_LOG.getDefaultState(), 3);
        }
    }

    private void generateLeaves(StructureWorldAccess world, List<BlockPos> leafNodes) {
        for (BlockPos node : leafNodes) {
            generateSmallLeafLayer(world, node.up(2));
            generateSmallLeafLayer(world, node.down(2));

            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    for (int y = -1; y <= 1; y++) {
                        //skip the edges
                        if (Math.abs(x) == 2 && Math.abs(z) == 2) {
                            continue;
                        }

                        //test and set
                        BlockPos local = node.add(x, y, z);
                        if (world.getBlockState(local).isAir()) {
                            world.setBlockState(local, Blocks.OAK_LEAVES.getDefaultState(), 0);
                        }
                    }
                }
            }
        }
    }

    private void generateSmallLeafLayer(WorldAccess world, BlockPos pos) {
        if (world.getBlockState(pos).isAir()) {
            world.setBlockState(pos, Blocks.OAK_LEAVES.getDefaultState(), 0);
        }

        for (Direction direction : Direction.Type.HORIZONTAL) {
            BlockPos local = pos.offset(direction);
            if (world.getBlockState(local).isAir()) {
                world.setBlockState(local, Blocks.OAK_LEAVES.getDefaultState(), 0);
            }
        }
    }
}