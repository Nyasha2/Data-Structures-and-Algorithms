package edu.caltech.cs2.coloring;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.ChainingHashSet;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.ISet;

import java.util.Iterator;

public class Program implements Iterable<Instruction> {
    private IDeque<Instruction> program;

    public Program(IDeque<Instruction> program) {
        this.program = program;
    }

    public Program color(IDictionary<Integer, Integer> coloring) {
       IDeque<Instruction> result = new ArrayDeque<>();
       for (Instruction instr : program) {
           result.add(new Instruction(instr.type(), coloring.get(instr.variable()) - 1));
       }
       return new Program(result);
    }

    public ISet<Integer> variables() {
        ISet<Integer> vars = new ChainingHashSet<>();
        for (Instruction instr : program) {
            vars.add(instr.variable());
        }
        return vars;
    }

    public NodeGraph constructInterferenceGraph() {

//
//        }
        NodeGraph g = new NodeGraph(variables().size());
        IDeque<Instruction> reverseProgram = new ArrayDeque<>();
        for (Instruction thisOne: this.program){
            reverseProgram.addFront(thisOne);
        }
        ISet<Integer> live = new ChainingHashSet<>();
        for (Instruction instr : reverseProgram) {
            int var = instr.variable();
            if (instr.type() == InstructionType.Read) {
                live.remove(var);
            } else if (instr.type() == InstructionType.Write) {
                live.add(var);
            }
            for (int other : live) {
                if (var != other) {
                    g.addEdge(var, other);
                }
            }
        }
        return g;
    }

    @Override
    public String toString() {
        return this.program.toString();
    }

    @Override
    public Iterator<Instruction> iterator() {
        return this.program.iterator();
    }
}
