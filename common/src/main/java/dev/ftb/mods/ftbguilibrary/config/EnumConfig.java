package dev.ftb.mods.ftbguilibrary.config;

import dev.ftb.mods.ftbguilibrary.icon.Color4I;
import dev.ftb.mods.ftbguilibrary.icon.Icon;
import dev.ftb.mods.ftbguilibrary.misc.ButtonListBaseScreen;
import dev.ftb.mods.ftbguilibrary.utils.MouseButton;
import dev.ftb.mods.ftbguilibrary.utils.TooltipList;
import dev.ftb.mods.ftbguilibrary.widget.BaseScreen;
import dev.ftb.mods.ftbguilibrary.widget.Panel;
import dev.ftb.mods.ftbguilibrary.widget.SimpleTextButton;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.Nullable;

/**
 * @author LatvianModder
 */
public class EnumConfig<E> extends ConfigWithVariants<E> {
	public final NameMap<E> nameMap;

	public EnumConfig(NameMap<E> nm) {
		nameMap = nm;
		defaultValue = nameMap.defaultValue;
		value = nameMap.defaultValue;
	}

	@Override
	public Component getStringForGUI(E v) {
		return nameMap.getDisplayName(v);
	}

	@Override
	public Color4I getColor(E v) {
		Color4I col = nameMap.getColor(v);
		return col.isEmpty() ? Tristate.DEFAULT.color : col;
	}

	@Override
	public void addInfo(TooltipList list) {
		super.addInfo(list);

		if (nameMap.size() > 0) {
			list.blankLine();

			for (E v : nameMap) {
				boolean e = isEqual(v, value);
				TextComponent c = new TextComponent(e ? "+ " : "- ");
				c.withStyle(e ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY);
				c.append(nameMap.getDisplayName(v));
				list.add(c);
			}
		}
	}

	@Override
	public void onClicked(MouseButton button, ConfigCallback callback) {
		if (nameMap.values.size() > 16 || BaseScreen.isCtrlKeyDown()) {
			ButtonListBaseScreen g = new ButtonListBaseScreen() {
				@Override
				public void addButtons(Panel panel) {
					for (E v : nameMap) {
						panel.add(new SimpleTextButton(panel, nameMap.getDisplayName(v), Icon.EMPTY) {
							@Override
							public void onClicked(MouseButton button) {
								playClickSound();
								setCurrentValue(v);
								callback.save(true);
							}
						});
					}
				}
			};

			g.setHasSearchBox(true);
			g.openGui();
			return;
		}

		super.onClicked(button, callback);
	}

	@Override
	public E getIteration(E v, boolean next) {
		return next ? nameMap.getNext(v) : nameMap.getPrevious(v);
	}

	@Override
	public Icon getIcon(@Nullable E v) {
		if (v != null) {
			Icon icon = nameMap.getIcon(v);

			if (!icon.isEmpty()) {
				return icon;
			}
		}

		return super.getIcon(v);
	}
}