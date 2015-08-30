package com.doctordark.base.command.module;

import com.doctordark.base.BasePlugin;
import com.doctordark.base.command.BaseCommandModule;
import com.doctordark.base.command.module.inventory.ClearInvCommand;
import com.doctordark.base.command.module.inventory.CopyInvCommand;
import com.doctordark.base.command.module.inventory.GiveCommand;
import com.doctordark.base.command.module.inventory.IdCommand;
import com.doctordark.base.command.module.inventory.InvSeeCommand;
import com.doctordark.base.command.module.inventory.ItemCommand;
import com.doctordark.base.command.module.inventory.MoreCommand;
import com.doctordark.base.command.module.inventory.SkullCommand;

public class InventoryModule extends BaseCommandModule {

    public InventoryModule(BasePlugin plugin) {
        commands.add(new ClearInvCommand());
        commands.add(new GiveCommand());
        commands.add(new IdCommand());
        commands.add(new InvSeeCommand(plugin));
        commands.add(new CopyInvCommand());
        commands.add(new ItemCommand());
        commands.add(new MoreCommand());
        commands.add(new SkullCommand());
    }
}
