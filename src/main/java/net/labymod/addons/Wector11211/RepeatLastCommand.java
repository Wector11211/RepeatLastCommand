package net.labymod.addons.Wector11211;

import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.labymod.settings.elements.*;
import net.minecraft.client.Minecraft;
import net.labymod.api.LabyModAddon;
import net.labymod.utils.Material;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class RepeatLastCommand extends LabyModAddon {
    private boolean addonEnabled;
    private int triggerHotkey;
    private boolean keyPressedFlag;

    @Override
    public void onEnable() {
        getApi().registerForgeListener(this);
    }

    @Override
    public void loadConfig() {
        this.addonEnabled = getConfig().has( "enabled" ) ? getConfig().get("enabled").getAsBoolean() : true;
        this.triggerHotkey = getConfig().has( "hotkey" ) ? Keyboard.getKeyIndex(getConfig().get("hotkey").getAsString()) : Keyboard.KEY_NONE;
    }

    @Override
    protected void fillSettings(List<SettingsElement> options) {

        BooleanElement addonEnabledElement = new BooleanElement(
                "Enabled",
                this,
                new ControlElement.IconData(Material.LEVER),
                "enabled", this.addonEnabled);

        KeyElement hotkeyElement = new KeyElement(
                "Hotkey",
                new ControlElement.IconData(Material.BOOK),
                this.triggerHotkey, accepted -> {
                    if ( accepted == -1 ) {
                        return;
                    }
                    this.triggerHotkey = accepted;
                    getConfig().addProperty("hotkey", Keyboard.getKeyName(this.triggerHotkey));
                    saveConfig();
                });

        options.add( addonEnabledElement );
        options.add( hotkeyElement );
    }

    @SubscribeEvent
    public void onKeyInput(TickEvent.ClientTickEvent e) {
        if(RepeatLastCommand.this.addonEnabled){
            if(Minecraft.getMinecraft().currentScreen == null) {
                if (Keyboard.isKeyDown(this.triggerHotkey)) {
                    if (!this.keyPressedFlag) {
                        this.keyPressedFlag = true;
                        List recentMessages = Minecraft.getMinecraft().ingameGUI.getChatGUI().getSentMessages();
                        if(!recentMessages.isEmpty()) {
                            String lastMessage = recentMessages.get(recentMessages.size() - 1).toString();
                            Minecraft.getMinecraft().player.sendChatMessage(lastMessage);
                        }
                    }
                } else {
                    this.keyPressedFlag = false;
                }
            }
        }
    }
}
