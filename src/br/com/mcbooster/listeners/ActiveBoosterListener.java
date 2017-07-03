package br.com.mcbooster.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.nossr50.datatypes.skills.SkillType;

import br.com.mcbooster.MCBooster;
import br.com.mcbooster.models.PlayerBooster;

public class ActiveBoosterListener implements Listener {

	private MCBooster plugin;
	private Inventory inventory;

	public ActiveBoosterListener(MCBooster plugin) {
		this.plugin = plugin;
		this.inventory = Bukkit.createInventory(null, 27, "Escolha a habilidade:");
		this.inventory.setItem(12, createItem(Material.DIAMOND_PICKAXE, "§aMineração", new String[] { "§7* Ative o booster nessa habilidade!" }, 1, 0));
		this.inventory.setItem(11, createItem(Material.BOW, "§aArqueiro", new String[] { "§7* Ative o booster nessa habilidade!" }, 1, 0));
		this.inventory.setItem(10, createItem(Material.DIAMOND_SWORD, "§aEspadas", new String[] { "§7* Ative o booster nessa habilidade!" }, 1, 0));
		this.inventory.setItem(14, createItem(Material.DIAMOND_SPADE, "§aEscavação", new String[] { "§7* Ative o booster nessa habilidade!" }, 1, 0));
		this.inventory.setItem(15, createItem(Material.ANVIL, "§aReparação", new String[] { "§7* Ative o booster nessa habilidade!" }, 1, 0));
		this.inventory.setItem(16, createItem(Material.DIAMOND_BOOTS, "§aAcrobacia", new String[] { "§7* Ative o booster nessa habilidade!" }, 1, 0));
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	private void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(player.getItemInHand() != null && player.getItemInHand().getTypeId() != 0 && player.getItemInHand().getType() == Material.EXP_BOTTLE && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().hasLore()){
			if(player.getItemInHand().getItemMeta().getDisplayName().equals("§aBooster de Experiência")){
				event.setCancelled(true);
				if(this.plugin.playerBoosterManager.hasBoosterActived(player.getName())){
					PlayerBooster playerBooster = this.plugin.playerBoosterManager.getPlayerBooster(player.getName());
					long time = playerBooster.getActivedTime() - System.currentTimeMillis();
					String timer = this.plugin.formatDifference(time);
					if(!timer.equals("agora")){
						player.sendMessage("§7* Você já possui um booster ativado na skill §f" + playerBooster.getSkillType().getName() + "§7.");
						player.sendMessage("§7* Tempo restante: §f" + timer + "§7.");
						return;
					}else{
						this.plugin.playerBoosterManager.removeBooster(player.getName());
					}
				}
				player.updateInventory();
				player.openInventory(this.inventory);
			}
		}
	}

	@EventHandler
	private void onClick(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player && event.getInventory().getName().equals(this.inventory.getName()) && event.getCurrentItem() != null && event.getCurrentItem().getTypeId() != 0){
			event.setCancelled(true);
			Player player = (Player) event.getWhoClicked();
			ItemStack item = event.getCurrentItem();
			if(!this.plugin.playerBoosterManager.hasBoosterActived(player.getName())){
				if(item.getItemMeta().getDisplayName().equals("§aMineração")){
					this.plugin.playerBoosterManager.setBooster(player.getName(), SkillType.MINING);
					player.sendMessage("§7* Parabéns, você ativou um booster na skill §fMineração§7!");
				}
				if(item.getItemMeta().getDisplayName().equals("§aArqueiro")){
					this.plugin.playerBoosterManager.setBooster(player.getName(), SkillType.ARCHERY);
					player.sendMessage("§7*Parabéns, você ativou um booster na skill §fArqueiro§7!");
				}
				if(item.getItemMeta().getDisplayName().equals("§aEspadas")){
					this.plugin.playerBoosterManager.setBooster(player.getName(), SkillType.SWORDS);
					player.sendMessage("§7* Parabéns, você ativou um booster na skill §fEspadas§7!");
				}
				if(item.getItemMeta().getDisplayName().equals("§aEscavação")){
					this.plugin.playerBoosterManager.setBooster(player.getName(), SkillType.EXCAVATION);
					player.sendMessage("§7* Parabéns, você ativou um booster na skill §fEscavação§7!");
				}
				if(item.getItemMeta().getDisplayName().equals("§aReparação")){
					this.plugin.playerBoosterManager.setBooster(player.getName(), SkillType.REPAIR);
					player.sendMessage("§7* Parabéns, você ativou um booster na skill §fReparação§7!");
				}
				if(item.getItemMeta().getDisplayName().equals("§aAcrobacia")){
					this.plugin.playerBoosterManager.setBooster(player.getName(), SkillType.ACROBATICS);
					player.sendMessage("§7* Parabéns, você ativou um booster na skill §fAcrobacia§7!");
				}
				if(player.getItemInHand().getAmount() - 1 > 1){
					ItemStack is = new ItemStack(Material.EXP_BOTTLE, player.getItemInHand().getAmount() - 1);
					ItemMeta itemMeta = is.getItemMeta();
					itemMeta.setDisplayName("§aBooster de Experiência");
					List<String> lore = new ArrayList<>();
					lore.add("§7* Receba §f1 hora §7de §fDuplo XP §7em qual quer habilidade!");
					itemMeta.setLore(lore);
					is.setItemMeta(itemMeta);
					player.setItemInHand(is);
				}else{
					player.setItemInHand(null);
				}
				player.closeInventory();
				player.updateInventory();
			}
		}
	}

	private ItemStack createItem(Material material, String nome, String[] lore, int quantidade, int metadata) {
		ItemStack item = new ItemStack(material, quantidade, (byte) metadata);
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(nome);
		List<String> itemLore = new ArrayList<>();
		if (lore != null) {
			for (String loree : lore) {
				itemLore.add(loree);
			}
			itemM.setLore(itemLore);
		}
		item.setItemMeta(itemM);
		return item;
	}

}
