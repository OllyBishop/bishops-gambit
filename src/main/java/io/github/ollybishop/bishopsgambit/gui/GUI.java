package io.github.ollybishop.bishopsgambit.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
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

import io.github.ollybishop.bishopsgambit.board.Board;
import io.github.ollybishop.bishopsgambit.board.Square;
import io.github.ollybishop.bishopsgambit.game.Game;
import io.github.ollybishop.bishopsgambit.game.Game.Status;
import io.github.ollybishop.bishopsgambit.io.Images;
import io.github.ollybishop.bishopsgambit.pieces.Piece;
import io.github.ollybishop.bishopsgambit.pieces.Piece.Typ;
import io.github.ollybishop.bishopsgambit.player.Player;
import io.github.ollybishop.bishopsgambit.player.Player.Colour;

public class GUI extends JFrame
{
    private static final Cursor DEFAULT_CURSOR = Cursor.getDefaultCursor();
    private static final Cursor HAND_CURSOR = Cursor.getPredefinedCursor( Cursor.HAND_CURSOR );

    // ============================================================================================
    // Components and Layouts
    // ============================================================================================

    private final JPanel contentPane = new JPanel();
    private final BorderLayout contentLayout = new BorderLayout();

    // ============================================================================================

    private final JPanel topMenuPane = new JPanel();
    private final JPanel bottomMenuPane = new JPanel();

    private final JButton newGameButton = new JButton( "New Game" );
    private final JButton flipViewButton = new JButton( "Flip View" );
    private final JToggleButton lockViewButton = new JToggleButton( "Lock View" );

    private final JButton previousMoveButton = new JButton( "Previous Move" );
    private final JButton nextMoveButton = new JButton( "Next Move" );

    // ============================================================================================

    /**
     * The "tabletop" is the area containing the chessboard and both sets of captured pieces.
     */
    private final JPanel tabletopPane = new JPanel();
    private final BorderLayout tabletopLayoutWhite = new BorderLayout();
    private final BorderLayout tabletopLayoutBlack = new BorderLayout();

    private final JPanel chessboardPane = new JPanel();
    private final GridBagLayout chessboardLayoutWhite = new GridBagLayout();
    private final GridBagLayout chessboardLayoutBlack = new GridBagLayout();

    private final JPanel capturedPiecesPaneWhite = new SortedJPanel();
    private final JPanel capturedPiecesPaneBlack = new SortedJPanel();
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

    /**
     * An integer representing the index of the board state currently displayed in the GUI. A value
     * of {@code 0} represents the initial board setup; subsequent values represent the board state
     * after each successive ply (half-move) has been completed.
     */
    private int boardIndex;

    private int latestBoardIndex;

    private Game game;

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

    private Player getActivePlayer()
    {
        return game.getActivePlayer();
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
        topMenuPane.add( flipViewButton );
        topMenuPane.add( lockViewButton );

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

    // ============================================================================================

    private void addSquareMouseListeners()
    {
        for ( SquareComp pressed : squareComps )
        {
            pressed.addMouseListener( new MouseAdapter()
            {
                /**
                 * Indicates whether this square was already selected when it was last pressed.
                 */
                boolean alreadySelected;

                @Override
                public void mousePressed( MouseEvent e )
                {
                    if ( boardIndex < latestBoardIndex )
                        return;

                    SquareComp fromBefore = from;
                    SquareComp toBefore = to;

                    alreadySelected = pressed == from || pressed == to;

                    boolean ownPieceSelected = getSquare( pressed ).isOccupiedBy( getActivePlayer() );

                    // If no squares were preselected
                    if ( from == null )
                    {
                        if ( ownPieceSelected )
                            selectFromSquare( pressed );
                    }
                    // If only a 'from' square was preselected
                    else if ( to == null )
                    {
                        if ( pressed != from )
                            if ( pressed.hasMoveMarker() )
                                selectToSquare( pressed );
                            else
                            {
                                deselectFromSquare();

                                if ( ownPieceSelected )
                                    selectFromSquare( pressed );
                            }
                    }
                    // If both 'from' and 'to' squares were preselected
                    else
                    {
                        if ( pressed != to )
                        {
                            deselectFromSquare();

                            if ( pressed != fromBefore && ownPieceSelected )
                                selectFromSquare( pressed );
                        }

                        deselectToSquare();
                    }

                    afterMouseEvent( fromBefore, toBefore );
                }

                @Override
                public void mouseReleased( MouseEvent e )
                {
                    if ( boardIndex < latestBoardIndex )
                        return;

                    // If no squares were preselected
                    if ( from == null )
                        return;

                    SquareComp fromBefore = from;
                    SquareComp toBefore = to;

                    int x = pressed.getX() + e.getX();
                    int y = pressed.getY() + e.getY();

                    // Find the component on which the mouse was released
                    Component comp = chessboardPane.getComponentAt( x, y );
                    SquareComp released = comp instanceof SquareComp ? (SquareComp) comp : null;

                    // If the mouse was released somewhere not on the board
                    if ( released == null )
                        deselectFromAndToSquares();
                    // If only a 'from' square was preselected
                    else if ( to == null )
                    {
                        if ( released == from )
                        {
                            if ( alreadySelected )
                                deselectFromSquare();
                        }
                        else if ( released != pressed )
                            if ( released.hasMoveMarker() )
                                selectToSquare( released );
                            else
                                deselectFromSquare();
                    }
                    // If both 'from' and 'to' squares were preselected
                    else
                    {
                        if ( released != to )
                            deselectFromAndToSquares();
                    }

                    afterMouseEvent( fromBefore, toBefore );
                }

                private void afterMouseEvent( SquareComp fromBefore, SquareComp toBefore )
                {
                    if ( from == fromBefore && to == toBefore )
                        return;

                    updateMoveMarkers();

                    if ( to != toBefore )
                    {
                        updateBoardState();
                        updateCheckBorder();
                    }
                }
            } );
        }
    }

    private void selectFromSquare( SquareComp squareComp )
    {
        from = squareComp;
        from.select();
    }

    private void selectToSquare( SquareComp squareComp )
    {
        to = squareComp;
        to.select();
    }

    private void deselectFromSquare()
    {
        if ( from != null )
        {
            from.deselect();
            from = null;
        }
    }

    private void deselectToSquare()
    {
        if ( to != null )
        {
            to.deselect();
            to = null;
        }
    }

    private void deselectFromAndToSquares()
    {
        deselectFromSquare();
        deselectToSquare();
    }

    private void updateMoveMarkers()
    {
        for ( SquareComp squareComp : squareComps )
            squareComp.showMoveMarker( false );

        if ( from != null && to == null )
        {
            for ( Square move : getSquare( from ).getPiece().getMoves( getBoard() ) )
                getSquareComp( move ).showMoveMarker( true );
        }
    }

    // ============================================================================================

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

        flipViewButton.addActionListener( e ->
        {
            if ( tabletopPane.getLayout() == tabletopLayoutWhite )
                orientTabletop( Colour.BLACK );
            else
                orientTabletop( Colour.WHITE );
        } );

        previousMoveButton.addActionListener( e ->
        {
            deselectFromAndToSquares();

            boardIndex--;
            updateBoardState();
            updatePieceCursors();
        } );

        nextMoveButton.addActionListener( e ->
        {
            boardIndex++;
            updateBoardState();
            updatePieceCursors();
        } );
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

                updateCheckSquare();
                updatePieceCursors();
                orientTabletop();

                checkIfGameIsOver();
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
                rescalePieceContainers();
                updateCheckBorder();
            }
        } );
    }

    private void newGame()
    {
        deselectFromAndToSquares();

        if ( check != null )
        {
            check.resetBorder();
            check = null;
        }

        // Remove all existing PieceComps from the GUI
        for ( PieceComp pieceComp : pieceComps )
        {
            Container parent = pieceComp.getParent();

            if ( parent != null )
                parent.remove( pieceComp );
        }

        pieceComps.clear();

        boardIndex = latestBoardIndex = 0;

        game = new Game();

        for ( Piece piece : getBoard().getPieces() )
            createPieceComp( piece );

        updateBoardState();
        updatePieceCursors();
        orientTabletop();
    }

    private void createPieceComp( Piece piece )
    {
        pieceComps.add( new PieceComp( piece ) );
    }

    // ============================================================================================
    // Methods for handling changes to the underlying game state
    // ============================================================================================

    private void makeMove()
    {
        Square fromSquare = getSquare( from );
        Square toSquare = getSquare( to );

        deselectFromAndToSquares();

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

        boardIndex = ++latestBoardIndex;

        // TODO: This can be removed once promoted pieces are included in move previews
        updateBoardState();
    }

    private void checkIfGameIsOver()
    {
        if ( game.isGameOver() )
        {
            Icon icon;

            if ( game.getStatus() == Status.CHECKMATE )
                icon = Images.createIcon( getActivePlayer().getColour().transpose(), Typ.KING );
            else
                icon = null;

            JOptionPane.showMessageDialog( this, game.getGameOverMessage(), "Game Over", JOptionPane.PLAIN_MESSAGE, icon );
        }
    }

    // ============================================================================================
    // Methods for managing the position, size and visibility of UI components
    // ============================================================================================

    private void updateBoardState()
    {
        Board board;

        if ( to == null )
            board = getBoard( boardIndex );
        else
            board = getBoard().cloneAndMove( getSquare( from ), getSquare( to ) );

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

        // Prevents pieces from being displayed in multiple locations simultaneously
        tabletopPane.repaint();

        previousMoveButton.setEnabled( boardIndex > 0 );
        nextMoveButton.setEnabled( boardIndex < latestBoardIndex );

        // Debug SquareComp layering issues caused by incorrect indexing
        for ( SquareComp squareComp : squareComps )
            squareComp.debugLayeringIssues();
    }

    private void rescalePieceContainers()
    {
        int scale = getUIScale();

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

        rescalePieces();
    }

    private void rescalePieces()
    {
        int scale = getUIScale();

        for ( PieceComp pieceComp : pieceComps )
        {
            if ( pieceComp.getParent() instanceof SquareComp )
                pieceComp.setScale( scale );
            else
                pieceComp.setScale( scale * 3 / 5 );
        }
    }

    private int getUIScale()
    {
        int min = Math.min( contentPane.getWidth(), contentPane.getHeight() );
        return Math.max( 10, min / 8 );
    }

    private void updateCheckSquare()
    {
        if ( game.getStatus() == Status.CHECK ||
             game.getStatus() == Status.CHECKMATE )
            check = getSquareComp( getBoard(), getActivePlayer().getKing() );
        else
            check = null;

        updateCheckBorder();
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

    /**
     * This method should be called each time any of the following happens:
     * <ul>
     * <li>A new game is started</li>
     * <li>The active player changes (i.e. due to a move being made)</li>
     * <li>The 'Previous Move' button is clicked</li>
     * <li>The 'Next Move' button is clicked</li>
     * </ul>
     */
    private void updatePieceCursors()
    {
        for ( PieceComp pieceComp : pieceComps )
        {
            if ( boardIndex == latestBoardIndex &&
                 pieceComp.getPiece().getPlayer() == getActivePlayer() )
                pieceComp.setCursor( HAND_CURSOR );
            else
                pieceComp.setCursor( DEFAULT_CURSOR );
        }
    }

    /**
     * Orients the tabletop to the active player's perspective, provided the 'Lock View' button is
     * not selected.
     */
    private void orientTabletop()
    {
        if ( !lockViewButton.isSelected() )
            orientTabletop( getActivePlayer().getColour() );
    }

    /**
     * Orients the tabletop to the perspective of the player with the given colour. For example, if
     * {@code colour == Colour.WHITE}, the board is oriented with 'a1' in the lower-left corner and
     * Black's captured pieces displayed below.
     * 
     * @param colour the colour of the player whose perspective the tabletop is oriented to
     */
    private void orientTabletop( Colour colour )
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
}
