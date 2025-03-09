# AI Tool Configuration Documentation

This document outlines the structure and elements of the `config.xml` file used to configure Syntaxy.

## Overview

The configuration file defines:
- AI models to be used
- Model aliases for easy reference
- Agents and their behaviors
- Routing logic between agents

## Configuration Structure

The `config.xml` file has a hierarchical structure with the following main sections:

```
<config>
    <models>
        <!-- Model definitions -->
        <!-- Model aliases -->
    </models>
    <agent>
        <!-- Agent definitions -->
    </agent>
</config>
```

## Models Section

This section defines the available AI models and their aliases.

### `<model>` Element

Defines a specific AI model to be used in the system.

**Attributes:**
- `provider`: The model provider (e.g., "bedrock")
- `name`: Human-readable name of the model
- `id`: Unique identifier for the model
- `inputCost`: Cost per 1 million tokens
- `outputCost`: Cost per 1 million tokens

**Example:**
```xml
<model provider="bedrock" name="Claude 3" id="anthropic.claude-3-haiku*" inputCost="3.0" outputCost="15.0"/>
```

### `<modelAlias>` Element

Creates an alias for a model for easy reference.

**Attributes:**
- `provider`: The model provider (must match a defined model)
- `name`: The model name (must match a defined model)
- `alias`: The alias name to use for this model

**Example:**
```xml
<modelAlias provider="bedrock" name="Claude 3" alias="Programming"/>
<modelAlias provider="bedrock" name="Claude 3" alias="General"/>
```

## Agents Section

This section defines the agents that will process user requests.

### `<agent>` Element

Defines an agent within the system.

**Attributes:**
- `id`: Unique identifier for the agent
- `type`: The agent type (e.g., "router")
- `model`: The model or model alias this agent uses
- `description`: Description of the agent's purpose

**Child Elements:**
- `<system>`: Contains the system prompt for the agent
- `<userPrompt/>`: Placeholder for the user's input

**Example:**
```xml
<agent id="main" type="router" model="General" description="Routes the users query to the appropriate agent">
    <system>
        <!-- System prompt content -->
    </system>
    <userPrompt/>
</agent>
```

### `<system>` Element

Contains the system prompt that defines the agent's behavior.

**Example:**
```xml
<system>
    For the following prompt decide witch agent to route the task to.
    
    <include id="agentX" description="Description of what the agent can handle"/>
    <include groupName="MainAgents"/>
</system>
```

### `<include>` Element

Includes other agents either individually by ID or as a group by group name.

**Attributes:**
- `id`: (Optional) The ID of a specific agent to include
- `description`: (Optional) Override description for the included agent
- `groupName`: (Optional) Name of a group of agents to include

**Behavior:**
- When using `id`: Can override the child agent's description with an optional `description` attribute
- When using `groupName`: Always uses the original descriptions from the child agents

**Examples:**
```xml
<include id="agentX" description="Description of what the agent can handle"/>
<include groupName="MainAgents"/>
```

### `<userPrompt/>` Element

A placeholder that will be replaced with the user's input prompt at runtime.

## Complete Example

```xml
<config>
    <models>
        <model provider="bedrock" name="Claude 3" id="anthropic.claude-3-haiku*" inputCost="3.0" outputCost="15.0"/>
        <modelAlias provider="bedrock" name="Claude 3" alias="Programming"/>
        <modelAlias provider="bedrock" name="Claude 3" alias="General"/>
    </models>
    <agent id="main" type="router" model="General" description="Routes the users query to the appropriate agent">
        <system>
            For the following prompt decide witch agent to route the task to.

            <include id="agentX" description="Description of what the agent can handle"/>
            <include groupName="MainAgents"/>
        </system>
        <userPrompt/>
    </agent>
</config>
```
