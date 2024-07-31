package edu.caltech.cs2.project08.bots;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.project08.board.ArrayBoard;
import edu.caltech.cs2.project08.game.Board;
import edu.caltech.cs2.project08.game.Evaluator;
import edu.caltech.cs2.project08.game.Move;

public class MinimaxSearcher<B extends Board> extends AbstractSearcher<B> {
    @Override
    public Move getBestMove(B board, int myTime, int opTime) {
        BestMove best = minimax(this.evaluator, board, ply);
        return best.move;
    }

    private static <B extends Board> BestMove minimax(Evaluator<B> evaluator, B board, int depth) {
        if (depth == 0 || board.isGameOver()){
            int score = evaluator.eval(board);
            return new BestMove(score);
        }
        int bestValue = -10000000;
        Move bestMove = null;
        for (Move move: board.getMoves()){
            board.makeMove(move);
            BestMove current = minimax(evaluator, board, depth - 1);
            int value = -current.score;
            if (value > bestValue){
                bestValue = value;
                bestMove = move;
            }
            board.undoMove();
        }
        return new BestMove(bestMove, bestValue);
    }
}