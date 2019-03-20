package net.kernal.spiderman.worker.extract.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.schema.rule.AlwaysTrueRule;
import net.kernal.spiderman.worker.extract.schema.rule.ContainsRule;
import net.kernal.spiderman.worker.extract.schema.rule.EndsWithRule;
import net.kernal.spiderman.worker.extract.schema.rule.EqualsRule;
import net.kernal.spiderman.worker.extract.schema.rule.RegexRule;
import net.kernal.spiderman.worker.extract.schema.rule.StartsWithRule;
import net.kernal.spiderman.worker.extract.schema.rule.UrlMatchRule;

public abstract class Page {

	private static final Logger logger = Logger.getLogger(Page.class.getName());
	
	private String name;
	private Extractor.Builder extractorBuilder;
	private UrlMatchRules rules;
	private Models models;
	private boolean isPersisted;// 是否持久化
	private Field.ValueFilter filter;//全局Filter，所有Model的所有Field都需要执行
	
	public Page(String name, Extractor.Builder extractorBuilder) {
		this(name);
		this.extractorBuilder = extractorBuilder;
	}
	
	public Page(String name) {
		this.name = name;
		this.rules = new UrlMatchRules();
		this.models = new Models(this.name);
		this.config(rules, models);
	}
	
	public Page setFilter(Field.ValueFilter filter) {
		this.filter = filter;
		return this;
	}
	public Field.ValueFilter getFilter() {
		return this.filter;
	}
	
	public Page setIsPersisted(boolean bool) {
		this.isPersisted = bool;
		return this;
	}
	
	/**
	 * 是否持久化，在每一次重新调度采集任务的时候，持久化的内容是不会被清除的，但是非持久化的页面内容都会被删除掉。
	 */
	public boolean isPersisted() {
		return this.isPersisted;
	}
	
	public Page setExtractorBuilder(Extractor.Builder builder) {
		this.extractorBuilder = builder;
		return this;
	}
	
	public boolean matches(Downloader.Request request) {
		boolean matched = false;
		if ("or".equalsIgnoreCase(rules.getPolicy())) {
			for (UrlMatchRule r : rules.all()) {
				if (r.matches(request)) {
					matched = true;
					break;
				}
			}
		} else {
			for (UrlMatchRule r : rules.all()) {
				matched = r.matches(request);
			}
		}
		
		logger.log(Level.INFO, "Page["+this.getName()+"]");
		
		return matched;
	}
	
	public String getName() {
		return this.name;
	}
	public UrlMatchRules getRules() {
		return this.rules;
	}
	public Models getModels() {
		return this.models;
	}
	
	public Extractor.Builder getExtractorBuilder() {
		return this.extractorBuilder;
	}
	
	protected abstract void config(UrlMatchRules rules, Models models);
	
	public static class Builder {
		private Page page;
		public Builder() {
			this.page = new Page("") {
				public void config(UrlMatchRules rules, Models models) {}
			};
		}
		
		public Builder setName(String name) {
			this.page.name = name;
			return this;
		}
		
		public Builder setExtractorBuilder(Extractor.Builder builder) {
			this.page.extractorBuilder = builder;
			return this;
		}
		
		public Builder addRule(UrlMatchRule rule) {
			this.page.rules.add(rule);
			return this;
		}
		
		public Page build() {
			return page;
		}
	}
	
	public static class UrlMatchRules {
		
		private String policy;
		private List<UrlMatchRule> rules;
		
		public UrlMatchRules() {
			this.rules = new ArrayList<UrlMatchRule>();
		}
		public void addNegativeEqualsRule(String url) {
			this.rules.add(new EqualsRule(url).setNegativeEnabled(true));
		}
		public void addNegativeRegexRule(String regex) {
			this.rules.add(new RegexRule(regex).setNegativeEnabled(true));
		}
		public void addNegativeStartsWithRule(String prefix) {
			this.rules.add(new StartsWithRule(prefix).setNegativeEnabled(true));
		}
		public void addNegativeEndsWithRule(String suffix) {
			this.rules.add(new EndsWithRule(suffix).setNegativeEnabled(true));
		}
		public void addNegativeContainsRule(String chars) {
			this.rules.add(new ContainsRule(chars).setNegativeEnabled(true));
		}
		public void addEqualsRule(String url) {
			this.rules.add(new EqualsRule(url));
		}
		public void addRegexRule(String regex) {
			this.rules.add(new RegexRule(regex));
		}
		public void addStartsWithRule(String prefix) {
			this.rules.add(new StartsWithRule(prefix));
		}
		public void addEndsWithRule(String suffix) {
			this.rules.add(new EndsWithRule(suffix));
		}
		public void addContainsRule(String chars) {
			this.rules.add(new ContainsRule(chars));
		}
		public void add(UrlMatchRule rule) {
			this.rules.add(rule);
		}
		public void addAlwaysTrue() {
			this.rules.add(new AlwaysTrueRule());
		}
		public List<UrlMatchRule> all() {
			return this.rules;
		}
		
		public String getPolicy() {
			return policy;
		}
		public void setPolicy(String policy) {
			this.policy = policy;
		}

		public String toString() {
			return "UrlMatchRules [policy=" + policy + ", rules=" + rules + "]";
		}
		
	}
	
	public static class Models {
		private String page;
		private List<Model> models;
		public Models(String page) {
			this.page = page;
			this.models = new ArrayList<Model>();
		}
		public Model addModel(String name) {
			Model model = new Model(page, name);
			this.models.add(model);
			logger.info("添加Model配置: [page=" + page + ", model=" + name + "]");
			return model;
		}
		public List<Model> all() {
			return this.models;
		}
	}

	public String toString() {
		return "Page [name=" + name + "]";
	}

}