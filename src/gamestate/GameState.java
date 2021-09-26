package gamestate;

import java.util.HashSet;
import java.util.Set;

import chessboard.TileUI;
import chesspieces.Piece;

public class GameState {

    private TileUI[] chessBoard;
    private MoveAlgorithm moveAlgorithm;

    private Piece whiteKing;
    private Piece blackKing;

    private Set<Piece> whitePieces;
    private Set<Piece> blackPieces;
    
    public GameState(TileUI[] chessBoard, MoveAlgorithm moveAlgorithm, boolean isWhiteSide) {
        this.chessBoard = chessBoard;
        this.moveAlgorithm = moveAlgorithm;

        if (isWhiteSide) {
            this.whiteKing = chessBoard[60].getAssignedPiece();
            this.blackKing = chessBoard[4].getAssignedPiece();
            this.whitePieces = saveChessPieces(48);
            this.blackPieces = saveChessPieces(0);
        } else {
            this.whiteKing = chessBoard[4].getAssignedPiece();
            this.blackKing = chessBoard[60].getAssignedPiece();
            this.whitePieces = saveChessPieces(0);
            this.blackPieces = saveChessPieces(48);
        }
    }

    public TileUI[] getChessBoard() {
        return chessBoard;
    }

    public Set<Integer> getAllEnemyMoves(boolean isWhiteSide) {
        Set<Piece> enemyAllPieces = isWhiteSide ? blackPieces : whitePieces;
        Set<Integer> results = new HashSet<Integer>();
        for (Piece piece : enemyAllPieces) {
            piece = chessBoard[piece.getPiecePosition()].getAssignedPiece();
            if (piece != null) {
                results.addAll(piece.getAllMoves(chessBoard));
            }
        }
        return results;
    }

    public Set<Integer> getAllLegalEnemyMoves(boolean isWhiteSide) {
        Set<Piece> alivePieces = isWhiteSide ? blackPieces : whitePieces;
        Set<Integer> results = new HashSet<Integer>();
        for (Piece piece : alivePieces) {
            piece = chessBoard[piece.getPiecePosition()].getAssignedPiece();
            if (piece != null) {
                results.addAll(piece.getAllLegalMoves(this, chessBoard, moveAlgorithm));
            }
        }
        return results;
    }

    /**
     * Returns whether the given side has their king check or not.
     * 
     * @param isWhiteSide true if white side, false if not.
     * @return true if the given side has their king check, false otherwise.
     */
    public boolean calcIfAllyKingIsCheck(boolean isWhiteSide) {
        Set<Piece> enemyAlivePieces = isWhiteSide ? blackPieces : whitePieces;
        Piece allyKing = isWhiteSide ? whiteKing : blackKing;

        for (Piece enemyPiece : enemyAlivePieces) {
            enemyPiece = chessBoard[enemyPiece.getPiecePosition()].getAssignedPiece();
            if (enemyPiece != null) {
                Set<Integer> allMoves = enemyPiece.getAllMoves(chessBoard);
                if (allMoves.contains(allyKing.getPiecePosition())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Saves the chess pieces to an array.  
     * 
     * @param index the starting index of a chessboard to iterate through to index + 16.
     * @return Piece[] representing a set of chess pieces.
     */
    private Set<Piece> saveChessPieces(int index) {
        Set<Piece> chessPieces = new HashSet<Piece>();
        for (int i = index; i < index + 16; i++) {
            chessPieces.add(chessBoard[i].getAssignedPiece());
        }
        return chessPieces;
    }
}
