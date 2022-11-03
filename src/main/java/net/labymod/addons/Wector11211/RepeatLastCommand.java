package net.labymod.addons.Wector11211;

import net.labymod.settings.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import net.labymod.api.LabyModAddon;
import net.labymod.utils.Material;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class RepeatLastCommand extends LabyModAddon {
    private boolean addonEnabled;
    private int triggerHotkey;
    private String testCommand;

    @Override
    public void onEnable() {
        this.getApi().registerForgeListener(this);
    }

    @Override
    public void loadConfig() {
        this.addonEnabled = getConfig().has( "enabled" ) ? getConfig().get("enabled").getAsBoolean() : true;
        this.triggerHotkey = getConfig().has( "hotkey" ) ? Keyboard.getKeyIndex(getConfig().get("hotkey").getAsString()) : Keyboard.KEY_NONE;
        this.testCommand = getConfig().has( "command" ) ? getConfig().get("command").getAsString() : "test";
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
                        System.out.println( "Set new key to NONE" );
                        return;
                    }
                    this.triggerHotkey = accepted;
                    getConfig().addProperty("hotkey", Keyboard.getKeyName(this.triggerHotkey));
                    saveConfig();
                });

        StringElement testCommandElement = new StringElement(
            "Test command",
            new ControlElement.IconData( Material.PAPER ),
            this.testCommand, accepted -> {
                    this.testCommand = accepted;
                    getConfig().addProperty("command", this.testCommand);
                    saveConfig();
            });

        options.add( addonEnabledElement );
        options.add( hotkeyElement );
        options.add( testCommandElement );
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        if(this.addonEnabled){
            if(Keyboard.isKeyDown(this.triggerHotkey)){
                Minecraft.getMinecraft().player.sendChatMessage(this.testCommand);
            }
        }
    }
}
