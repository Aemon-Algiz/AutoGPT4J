package com.autogpt4j.command;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public abstract class Command {

    private String name;

    private String description;

    private String method;

    private String signature;

    private boolean enabled;

    private String disabledReason;

    public abstract String execute();

}
