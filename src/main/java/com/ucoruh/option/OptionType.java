package com.ucoruh.option;

/**
 * 
 * Enumeration class to define all available command line options.
 */
public enum OptionType {

	NONE("notset"), MERGEPAGES(ControllerOptions.MERGEPAGES_OPTION), BUILDPAGES(ControllerOptions.BUILDPAGES_OPTION),
	DEPLOYPAGES(ControllerOptions.DEPLOYPAGES_OPTION), GENERATEPAGES(ControllerOptions.GENERATEPAGES_OPTION),
	CLEANPAGES(ControllerOptions.CLEANPAGES_OPTION), DRAWIOEXPORT(ControllerOptions.DRAWIOEXPORT_OPTION),
	HELP(ControllerOptions.HELP_OPTION);

	private String commandName;

	private OptionType(String commandName) {
		this.commandName = commandName;
	}

	/**
	 * Get the command name of the option.
	 * 
	 * @return The command name of the option.
	 */
	public String getCommandName() {
		return commandName;
	}

}
