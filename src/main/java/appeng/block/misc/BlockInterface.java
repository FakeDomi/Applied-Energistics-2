/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.block.misc;


import appeng.api.util.IOrientable;
import appeng.block.AEBaseTileBlock;
import appeng.tile.misc.TileInterface;
import appeng.util.Platform;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class BlockInterface extends AEBaseTileBlock<TileInterface>
{

	private static final BooleanProperty OMNIDIRECTIONAL = BooleanProperty.create( "omnidirectional" );

	public BlockInterface()
	{
		super( Properties.create(Material.IRON) );
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(OMNIDIRECTIONAL);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
		// Determine whether the interface is omni-directional or not
		TileInterface te = this.getTileEntity(world, pos);
		boolean omniDirectional = true; // The default
		if (te != null) {
			omniDirectional = te.isOmniDirectional();
		}

		return super.updatePostPlacement(state, facing, facingState, world, pos, facingPos)
				.with(OMNIDIRECTIONAL, omniDirectional);
	}

	@Override
	public ActionResultType onActivated(final World w, final BlockPos pos, final PlayerEntity p, final Hand hand, final @Nullable ItemStack heldItem, final BlockRayTraceResult hit)
	{
		if( p.isCrouching() )
		{
			return ActionResultType.PASS;
		}

		final TileInterface tg = this.getTileEntity( w, pos );
		if( tg != null )
		{
			if( Platform.isServer() )
			{
				// FIXME Platform.openGUI( p, tg, AEPartLocation.fromFacing(hit), GuiBridge.GUI_INTERFACE );
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	protected boolean hasCustomRotation()
	{
		return true;
	}

	@Override
	protected void customRotateBlock( final IOrientable rotatable, final Direction axis )
	{
		if( rotatable instanceof TileInterface )
		{
			( (TileInterface) rotatable ).setSide( axis );
		}
	}
}
