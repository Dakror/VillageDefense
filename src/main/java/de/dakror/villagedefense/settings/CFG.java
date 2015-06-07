/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.villagedefense.settings;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import de.dakror.gamesetup.util.Helper;

/**
 * @author Dakror
 */
public class CFG {
	public static final File DIR = new File(System.getProperty("user.home") + "/.dakror/VillageDefense");
	public static final int TICK_TIMEOUT = 33;
	
	// -- UniVersion -- //
	public static final int VERSION = 2014010815;
	public static final int PHASE = 3;
	
	static long time = 0;
	
	public static String USERNAME;
	
	public static void init() {
		try {
			DIR.mkdirs();
			new File(DIR, "saves").mkdir();
			
			File us = new File(DIR, "username");
			if (!us.exists() || us.length() == 0) {
				USERNAME = JOptionPane.showInputDialog("Bitte gib deinen Benutzernamen an.");
				
				if (USERNAME == null) System.exit(0);
				
				us.createNewFile();
				
				Helper.setFileContent(us, USERNAME);
			} else {
				USERNAME = Helper.getFileContent(us);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// -- debug profiling -- //
	public static void u() {
		if (time == 0) time = System.currentTimeMillis();
		else {
			p(System.currentTimeMillis() - time);
			time = 0;
		}
	}
	
	public static void p(Object... p) {
		if (p.length == 1) System.out.println(p[0]);
		else System.out.println(Arrays.toString(p));
	}
}
