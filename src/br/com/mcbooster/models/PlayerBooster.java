package br.com.mcbooster.models;

import java.util.concurrent.TimeUnit;

import com.gmail.nossr50.datatypes.skills.SkillType;

public class PlayerBooster {
	
	private String nick;
	private SkillType skillType;
	private long activedTime;
	
	public PlayerBooster(String nick, SkillType skillType) {
		this.nick = nick;
		this.skillType = skillType;
		this.activedTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60);
	}
	
	public PlayerBooster(String nick, String skillType, long activedTime) {
		this.nick = nick;
		this.skillType = SkillType.getSkill(skillType);
		this.activedTime = activedTime;
	}
	
	public String getNick() {
		return nick;
	}
	
	public SkillType getSkillType() {
		return skillType;
	}
	
	public long getActivedTime() {
		return activedTime;
	}
	
}
