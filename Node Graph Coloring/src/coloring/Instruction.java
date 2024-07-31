package edu.caltech.cs2.coloring;

public record Instruction(InstructionType type, int variable) {
    @Override
    public String toString() {
        return (type == InstructionType.Read ? "r" : "w") + variable;
    }
}