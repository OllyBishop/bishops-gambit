package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import main.java.board.Board;
import main.java.board.Square;
import main.java.game.Game;
import main.java.game.Game.Status;
import main.java.io.Images;
import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
import main.java.player.Player;
import main.java.player.Player.Colour;

public class GUI extends JFrame
{
    private static final int DRAG_ACTIVATION_DELAY_MILLIS = 200;

    // ============================================================================================
    // Components and Layouts
    // ============================================================================================

    private final JPanel contentPane = new JPanel();
    private final BorderLayout contentLayout = new BorderLayout();

    // ============================================================================================

    private final JPanel topMenuPane = new JPanel();
    private final JPanel bottomMenuPane = new JPanel();

    private final JButton newGameButton = new JButton( "New Game" );
    private final JButton flipBoardButton = new JButton( "Flip Board" );
    private final JToggleButton lockBoardButton = new JToggleButton( "Lock Board" );

    private final JButton previousMoveButton = new JButton( "Previous Move" );
    private final JButton nextMoveButton = new JButton( "Next Move" );

    private boolean showingHistoricalBoardState;

    // ============================================================================================

    private final JPanel tabletopPane = new JPanel();
    private final BorderLayout tabletopLayoutWhite = new BorderLayout();
    private final BorderLayout tabletopLayoutBlack = new BorderLayout();

    private final JPanel chessboardPane = new JPanel();
    private final GridBagLayout chessboardLayoutWhite = new GridBagLayout();
    private final GridBagLayout chessboardLayoutBlack = new GridBagLayout();

    private final JPanel capturedPiecesPaneWhite = new OrderedJPanel();
    private final JPanel capturedPiecesPaneBlack = new OrderedJPanel();
    private final FlowLayout capturedPiecesLayoutWhite = new FlowLayout();
    private final FlowLayout capturedPiecesLayoutBlack = new FlowLayout();

    // ============================================================================================

    private final List<SquareComp> squareComps = createSquareComps();

    private List<SquareComp> createSquareComps()
    {
        List<SquareComp> squareComps = new ArrayList<>();

        for ( char file = 'a'; file <= 'h'; file++ )
            for ( char rank = '1'; rank <= '8'; rank++ )
                squareComps.add( new SquareComp( file, rank ) );

        return Collections.unmodifiableList( squareComps );
    }

    private SquareComp from;
    private SquareComp to;
    private SquareComp check;

    private final List<PieceComp> pieceComps = new ArrayList<>();

    // ============================================================================================
    // Non-Component Fields
    // ============================================================================================

    private Game game;

    /**
     * An integer representing the index of the board currently displayed in the GUI.
     * <p>
     * For example, a value of {@code 0} represents the board state at the beginning of the game, a
     * value of {@code 10} represents the board state after ten turns have been taken, etc. A
     * special case is {@code -1}, which indicates a move preview.
     * <p>
     * We therefore have the following inequalities (which are always true):
     * <ul>
     * <li>{@code boardIndex >= -1}</li>
     * <li>{@code numberOfTurnsTaken >= 0}</li>
     * <li>{@code boardIndex <= numberOfTurnsTaken}</li>
     * </ul>
     */
    private int boardIndex;

    // ============================================================================================
    // Map Methods
    // ============================================================================================

    private Square getSquare( SquareComp squareComp )
    {
        return getBoard().get( squareComp.getIndex() );
    }

    private SquareComp getSquareComp( Square square )
    {
        return squareComps.get( square.getIndex() );
    }

    private SquareComp getSquareComp( Board board, Piece piece )
    {
        Square square = piece.getSquare( board );

        if ( square != null )
            return getSquareComp( square );

        return null;
    }

    // ============================================================================================
    // Derived Methods
    // ============================================================================================

    private Board getBoard()
    {
        return game.getBoard();
    }

    private Board getBoard( int index )
    {
        return game.getBoard( index );
    }

    private int getNumberOfTurnsTaken()
    {
        return game.getNumberOfTurnsTaken();
    }

    private Player getActivePlayer()
    {
        return game.getActivePlayer();
    }

    private List<Square> getMoves( SquareComp squareComp )
    {
        return getSquare( squareComp ).getPiece().getMoves( getBoard() );
    }

    // ============================================================================================
    // Constructor and Associated Methods
    // ============================================================================================

    public GUI()
    {
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize( 640, 960 );

        addComponentsToFrame();
        configureLayouts();

        addSquareMouseListeners();
        addMenuButtonActionListeners();
        registerMoveConfirmationInput();
        addFrameResizedListener();

        newGame();
    }

    private void addComponentsToFrame()
    {
        add( contentPane );

        contentPane.add( tabletopPane );
        contentPane.add( topMenuPane );
        contentPane.add( bottomMenuPane );

        topMenuPane.add( newGameButton );
        topMenuPane.add( flipBoardButton );
        topMenuPane.add( lockBoardButton );

        bottomMenuPane.add( previousMoveButton );
        bottomMenuPane.add( nextMoveButton );

        tabletopPane.add( chessboardPane );
        tabletopPane.add( capturedPiecesPaneWhite );
        tabletopPane.add( capturedPiecesPaneBlack );

        for ( SquareComp squareComp : squareComps )
            chessboardPane.add( squareComp );
    }

    private void configureLayouts()
    {
        contentLayout.addLayoutComponent( tabletopPane, BorderLayout.CENTER );
        contentLayout.addLayoutComponent( topMenuPane, BorderLayout.NORTH );
        contentLayout.addLayoutComponent( bottomMenuPane, BorderLayout.SOUTH );

        tabletopLayoutWhite.addLayoutComponent( chessboardPane, BorderLayout.CENTER );
        tabletopLayoutWhite.addLayoutComponent( capturedPiecesPaneWhite, BorderLayout.NORTH );
        tabletopLayoutWhite.addLayoutComponent( capturedPiecesPaneBlack, BorderLayout.SOUTH );

        tabletopLayoutBlack.addLayoutComponent( chessboardPane, BorderLayout.CENTER );
        tabletopLayoutBlack.addLayoutComponent( capturedPiecesPaneWhite, BorderLayout.SOUTH );
        tabletopLayoutBlack.addLayoutComponent( capturedPiecesPaneBlack, BorderLayout.NORTH );

        for ( SquareComp squareComp : squareComps )
        {
            GridBagConstraints chessboardConstraintsWhite = new GridBagConstraints();
            GridBagConstraints chessboardConstraintsBlack = new GridBagConstraints();

            char file = squareComp.getFile();
            char rank = squareComp.getRank();

            chessboardConstraintsWhite.gridx = file - 'a';
            chessboardConstraintsWhite.gridy = '8' - rank;

            chessboardConstraintsBlack.gridx = 'h' - file;
            chessboardConstraintsBlack.gridy = rank - '1';

            chessboardLayoutWhite.setConstraints( squareComp, chessboardConstraintsWhite );
            chessboardLayoutBlack.setConstraints( squareComp, chessboardConstraintsBlack );
        }

        contentPane.setLayout( contentLayout );

        capturedPiecesPaneWhite.setLayout( capturedPiecesLayoutWhite );
        capturedPiecesPaneBlack.setLayout( capturedPiecesLayoutBlack );
    }

    private void addSquareMouseListeners()
    {
        for ( SquareComp pressed : squareComps )
        {
            pressed.addMouseListener( new MouseAdapter()
            {
                LocalDateTime lastPressed;

                @Override
                public void mousePressed( MouseEvent e )
                {
                    if ( showingHistoricalBoardState )
                        return;

                    // If a 'from' square is preselected (but a 'to' square isn't) and the 'from' square was pressed
                    // again, then return – we don't want to deselect the 'from' square (unless it is released again
                    // shortly afterwards, which will be handled by the mouse released event)
                    if ( pressed == from && to == null )
                        return;

                    lastPressed = LocalDateTime.now();

                    doSquareClickedAction( pressed );
                }

                @Override
                public void mouseReleased( MouseEvent e )
                {
                    if ( showingHistoricalBoardState )
                        return;

                    long pressDuration = ChronoUnit.MILLIS.between( lastPressed, LocalDateTime.now() );

                    int x = pressed.getX() + e.getX();
                    int y = pressed.getY() + e.getY();

                    // Find the component on which the mouse was released
                    Component released = chessboardPane.getComponentAt( x, y );

                    // If a 'from' square is preselected and a valid 'to' square was pressed but the mouse was
                    // released somewhere else, then deselect both squares
                    if ( pressed == to && released != to )
                        doSquareClickedAction( from );

                    if ( from == null || to != null )
                        return;

                    // If the mouse was released somewhere on the board (i.e. on a square)
                    if ( released instanceof SquareComp )
                    {
                        // If a square was pressed and released again quickly enough (less than 0.2 seconds), then
                        // return – we only want to select (or deselect) the pressed square
                        if ( pressed == released && pressDuration <= DRAG_ACTIVATION_DELAY_MILLIS )
                            return;

                        // If the mouse was released on a square the dragged piece can legally move to
                        if ( getMoves( from ).contains( getSquare( (SquareComp) released ) ) )
                            doSquareClickedAction( (SquareComp) released );
                        else
                            doSquareClickedAction( from );
                    }
                    // If the mouse was released somewhere not on the board, then deselect both squares
                    else
                    {
                        doSquareClickedAction( from );
                    }
                }
            } );
        }
    }

    private void addMenuButtonActionListeners()
    {
        newGameButton.addActionListener( e ->
        {
            int i = JOptionPane.showConfirmDialog( rootPane,
                                                   "Are you sure you want to start a new game?",
                                                   "New Game",
                                                   JOptionPane.YES_NO_OPTION );

            if ( i == JOptionPane.YES_OPTION )
                newGame();
        } );

        flipBoardButton.addActionListener( e ->
        {
            if ( tabletopPane.getLayout() == tabletopLayoutWhite )
                orientBoard( Colour.BLACK );
            else
                orientBoard( Colour.WHITE );
        } );

        previousMoveButton.addActionListener( e ->
        {
            if ( from != null )
                doSquareClickedAction( from );

            updateBoardState( getBoard( --boardIndex ) );
        } );

        nextMoveButton.addActionListener( e -> updateBoardState( getBoard( ++boardIndex ) ) );
    }

    private void registerMoveConfirmationInput()
    {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( e ->
        {
            if ( e.getKeyCode() == KeyEvent.VK_SPACE &&
                 e.getID() == KeyEvent.KEY_PRESSED &&
                 to != null )
            {
                makeMove();
                checkGameStatus();
                orientBoard();
                updateCheckBorder();
            }

            return true;
        } );
    }

    private void addFrameResizedListener()
    {
        addComponentListener( new ComponentAdapter()
        {
            /**
             * This method is called once when the frame is initialised (after the constructor call) and
             * each time the frame is resized thereafter.
             */
            @Override
            public void componentResized( ComponentEvent e )
            {
                rescalePieces();
                rescalePieceContainers();
                updateCheckBorder();
            }
        } );
    }

    private void newGame()
    {
        if ( from != null )
            doSquareClickedAction( from );

        if ( check != null )
            check = check.resetBorder();

        // Remove all existing PieceComps from the GUI
        for ( PieceComp pieceComp : pieceComps )
        {
            Container parent = pieceComp.getParent();

            if ( parent != null )
                parent.remove( pieceComp );
        }

        pieceComps.clear();

        game = new Game();

        for ( Piece piece : getBoard().getPieces() )
            createPieceComp( piece );

        orientBoard();
        updateBoardState();
    }

    // ============================================================================================
    // Methods
    // ============================================================================================

    private void createPieceComp( Piece piece )
    {
        pieceComps.add( new PieceComp( piece ) );
    }

    private void doSquareClickedAction( SquareComp squareComp )
    {
        if ( from != null && to == null )
        {
            for ( SquareComp sqComp : squareComps )
                sqComp.showCircle( false );
        }

        Square square = getSquare( squareComp );

        boolean deselectFrom = false;
        boolean selectFrom = false;
        boolean selectTo = false;

        if ( square.isOccupiedBy( getActivePlayer() ) )
        {
            deselectFrom = true;

            if ( from != squareComp )
                selectFrom = true;
        }
        else if ( to != null )
        {
            if ( to != squareComp )
                deselectFrom = true;
        }
        else if ( from != null )
        {
            if ( getMoves( from ).contains( square ) )
                selectTo = true;
            else
                deselectFrom = true;
        }

        SquareComp toBefore = to;

        if ( deselectFrom && from != null )
            from = from.deselect();

        if ( to != null )
            to = to.deselect();

        if ( selectFrom )
            from = squareComp.select();

        if ( selectTo )
            to = squareComp.select();

        // If 'to' was selected (or unselected), preview the selected move (or undo the preview)
        if ( to != toBefore )
        {
            updateBoardState();
            updateCheckBorder();
        }

        if ( from != null && to == null )
        {
            for ( Square sq : getMoves( from ) )
                getSquareComp( sq ).showCircle( true );
        }
    }

    /**
     * Orients the board to the active player's perspective, provided the 'Lock Board' button is not
     * selected.
     */
    private void orientBoard()
    {
        if ( lockBoardButton.isSelected() )
            return;

        orientBoard( getActivePlayer().getColour() );
    }

    /**
     * Orients the board to the player with the given <b>colour</b>'s perspective. For example, if
     * <b>colour</b> is {@code Colour.WHITE}, the board is oriented such that 'a1' is in the
     * lower-left corner, and the captured pieces belonging to Black are displayed underneath.
     * 
     * @param colour the colour of the player whose perspective the board is oriented to
     */
    private void orientBoard( Colour colour )
    {
        switch ( colour )
        {
            case WHITE:
                tabletopPane.setLayout( tabletopLayoutWhite );
                chessboardPane.setLayout( chessboardLayoutWhite );

                for ( SquareComp squareComp : squareComps )
                {
                    squareComp.showRank( 'a' );
                    squareComp.showFile( '1' );
                }

                break;

            case BLACK:
                tabletopPane.setLayout( tabletopLayoutBlack );
                chessboardPane.setLayout( chessboardLayoutBlack );

                for ( SquareComp squareComp : squareComps )
                {
                    squareComp.showRank( 'h' );
                    squareComp.showFile( '8' );
                }

                break;
        }
    }

    private void makeMove()
    {
        Square fromSquare = getSquare( from );
        Square toSquare = getSquare( to );

        if ( fromSquare.getPiece().canPromote( toSquare ) )
        {
            int i = JOptionPane.showOptionDialog( rootPane,
                                                  "Select a piece to promote to.",
                                                  "Promotion",
                                                  JOptionPane.DEFAULT_OPTION,
                                                  JOptionPane.PLAIN_MESSAGE,
                                                  Images.createIcon( getActivePlayer().getColour(), Typ.PAWN ),
                                                  Typ.PROMOTION_TYPES,
                                                  null );

            Typ newType = i == JOptionPane.CLOSED_OPTION ? Typ.QUEEN
                                                         : Typ.PROMOTION_TYPES[ i ];

            Piece newPiece = game.makeMove( fromSquare, toSquare, newType );

            createPieceComp( newPiece );
        }
        else
        {
            game.makeMove( fromSquare, toSquare );
        }

        from = from.deselect();
        to = to.deselect();

        // TODO: This can be removed once promoted pieces are included in move previews
        updateBoardState();
    }

    private void checkGameStatus()
    {
        Status status = game.getStatus();

        if ( status == Status.CHECK ||
             status == Status.CHECKMATE )
            check = getSquareComp( getBoard(), getActivePlayer().getKing() );
        else
            check = null;

        if ( game.isGameOver() )
        {
            Icon icon = status == Status.CHECKMATE ? Images.createIcon( getActivePlayer().getColour().transpose(), Typ.KING )
                                                   : null;

            JOptionPane.showMessageDialog( this, game.getGameOverMessage(), "Game Over", JOptionPane.PLAIN_MESSAGE, icon );
        }
    }

    private int getScale()
    {
        int min = Math.min( contentPane.getWidth(), contentPane.getHeight() );
        return Math.max( 10, min / 8 );
    }

    private void rescalePieces()
    {
        int scale = getScale();

        for ( PieceComp pieceComp : pieceComps )
        {
            if ( pieceComp.getParent() instanceof SquareComp )
                pieceComp.setScale( scale );
            else
                pieceComp.setScale( scale * 3 / 5 );
        }
    }

    private void rescalePieceContainers()
    {
        int scale = getScale();

        for ( SquareComp squareComp : squareComps )
            squareComp.setScale( scale );

        Dimension dimension = new Dimension( scale * 8, scale );

        capturedPiecesPaneWhite.setPreferredSize( dimension );
        capturedPiecesPaneBlack.setPreferredSize( dimension );

        /*
         * The FlowLayout class does not have an option to set the vertical alignment of its components.
         * To achieve this, we set the vertical gap to half of the remaining vertical space in the pane.
         * vgap = (paneHeight - compHeight) / 2 = (scale - scale * 3 / 5) / 2 = scale / 5
         */
        int gap = scale / 5;

        capturedPiecesLayoutWhite.setVgap( gap );
        capturedPiecesLayoutBlack.setVgap( gap );

        // Horizontal space between pieces
        capturedPiecesLayoutWhite.setHgap( -gap );
        capturedPiecesLayoutBlack.setHgap( -gap );
    }

    private void updateBoardState()
    {
        Board board;

        if ( to == null )
        {
            board = getBoard();
            boardIndex = getNumberOfTurnsTaken();
        }
        else
        {
            board = getBoard().cloneAndMove( getSquare( from ), getSquare( to ) );
            boardIndex = -1;
        }

        updateBoardState( board );
    }

    private void updateBoardState( Board board )
    {
        for ( PieceComp pieceComp : pieceComps )
        {
            SquareComp squareComp = getSquareComp( board, pieceComp.getPiece() );

            // If the piece is no longer on the board (i.e. it has been captured)
            if ( squareComp == null )
            {
                switch ( pieceComp.getPiece().getColour() )
                {
                    case WHITE:
                        capturedPiecesPaneWhite.add( pieceComp );
                        break;

                    case BLACK:
                        capturedPiecesPaneBlack.add( pieceComp );
                        break;
                }
            }
            else
            {
                squareComp.addPieceComp( pieceComp );
            }
        }

        rescalePieces();

        /*
         * Necessary to prevent UI issues. Without this, the selected piece may appear on both the
         * 'from' and 'to' square during move previews.
         */
        contentPane.repaint();

        showingHistoricalBoardState = 0 <= boardIndex && boardIndex < getNumberOfTurnsTaken();

        previousMoveButton.setEnabled( boardIndex != 0 && getNumberOfTurnsTaken() > 0 );
        nextMoveButton.setEnabled( showingHistoricalBoardState );
    }

    private void updateCheckBorder()
    {
        if ( check == null )
            return;

        if ( to == null )
        {
            int thickness = Math.max( 1, check.getWidth() / 20 );
            Border border = BorderFactory.createLineBorder( Color.RED, thickness );
            check.setBorder( border );
        }
        else
        {
            check.resetBorder();
        }
    }
}
