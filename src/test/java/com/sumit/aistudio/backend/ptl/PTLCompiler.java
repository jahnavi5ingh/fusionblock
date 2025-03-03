package com.sumit.aistudio.backend.ptl;

import com.sumit.aistudio.backend.agents.OllamaAgent;
import com.sumit.aistudio.backend.linker.LinkInfoService;
import com.sumit.aistudio.backend.prompt.PromptService;
import com.sumit.aistudio.backend.ptl.nodes.GenInfoNode;
import com.sumit.aistudio.backend.ptl.nodes.GeneratedNode;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;

@SpringBootApplication
public class PTLCompiler {
	@Autowired LinkInfoService linkInfoService;
	@Autowired
	PromptLinker linkerInfo;
	@Autowired
	PromptService promptService;
	@Autowired
    OllamaAgent agent;
	static final Map<String,Object > arguments = new HashMap<>();
	public static void main(String[] args) throws ParseException {

		Options options = new Options();
		options.addOption(new Option("p", "promptfile", true, "prompt file to compile."));
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption("promptfile") ) {
			arguments.put("promptfile",cmd.getOptionValue("promptfile"));
		}else{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Prompt compiler", options);
			exit(1);
		}
		SpringApplication.run(PTLCompiler.class, args);
	}
	String file = null;
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() throws IOException {
		System.out.println("hello world, I have just started up");
		System.out.printf("Going to compile: "+arguments.get("promptfile"));
		PromptFileParser tester = new PromptFileParser();
		PromtCompilerGenerator gen = new PromtCompilerGenerator();
		String base = "C:/projects/aibackend/src/main/resources/ptl/";

		if(arguments.get("promptfile")!=null) {
			String[] files = StringUtils.split(arguments.get("promptfile").toString(), ",");
			for (String file : files) {
				GenInfoNode node = tester.parseFile(base + file, linkerInfo);
				System.out.println(node);
				GeneratedNode rootGen = gen.generate(node, linkerInfo,promptService,agent);
				System.out.println(rootGen.getInfo());
			}
			//create linkinfo from linker promptoutputs and save via linkinfo service

			linkInfoService.saveLinkInfo(linkerInfo);
		}
	}

}
