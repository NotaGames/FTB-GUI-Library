package dev.ftb.mods.ftblibrary.net;

import dev.ftb.mods.ftblibrary.nbtedit._NBTEditorScreen;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class EditNBTPacket extends BasePacket {
	private final CompoundTag info;
	private final CompoundTag tag;

	public EditNBTPacket(FriendlyByteBuf buf) {
		info = buf.readNbt();
		tag = buf.readAnySizeNbt();
	}

	public EditNBTPacket(CompoundTag i, CompoundTag t) {
		info = i;
		tag = t;
	}

	@Override
	public PacketID getId() {
		return FTBLibraryNet.EDIT_NBT;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeNbt(info);
		buf.writeNbt(tag);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		new _NBTEditorScreen(info, tag).openGui();
	}
}
