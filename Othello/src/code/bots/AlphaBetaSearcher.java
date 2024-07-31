package edu.caltech.cs2.project08.bots;

import edu.caltech.cs2.project08.game.Board;
import edu.caltech.cs2.project08.game.Evaluator;
import edu.caltech.cs2.project08.game.Move;

public class AlphaBetaSearcher<B extends Board> extends AbstractSearcher<B> {
    @Override
    public Move getBestMove(B board, int myTime, int opTime) {
        BestMove best = alphaBeta(this.evaluator, board, ply);
        return best.move;
    }

    private static <B extends Board> BestMove alphaBeta(Evaluator<B> evaluator, B board, int depth) {
        return alphaBetaHelper(evaluator, board, depth, -10000000, 10000000);
    }

    private static <B extends Board> BestMove alphaBetaHelper(Evaluator<B> evaluator, B board, int depth, int alpha, int beta){
        if (depth == 0 || board.isGameOver()){
            return new BestMove(evaluator.eval(board));
        }
        Move bestMove = null;
        for (Move move: board.getMoves()){
            board.makeMove(move);
            int value = -alphaBetaHelper(evaluator, board, depth - 1, -beta, -alpha).score;
            board.undoMove();
            if (value > alpha){
                alpha = value;
                bestMove = move;
            }
            if (alpha >= beta){
                break;
            }
        }
        return new BestMove(bestMove, alpha);
    }
}