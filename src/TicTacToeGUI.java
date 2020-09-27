import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class TicTacToeGUI extends JFrame
{
    private Container pane;
    private String currentPlayer;
    private JButton[][] board;
    private boolean winner;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem newGame;
    private int size;
    private int screenSize;
    private String user;
    private String ai;

    public TicTacToeGUI()
    {
        super();
        this.pane = getContentPane();
        this.size = 3;
        this.screenSize = 500;
        this.pane.setLayout(new GridLayout(this.size, this.size));
        setTitle("Tic Tac Toe (with AI)");
        setSize(this.screenSize, this.screenSize);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        this.board = new JButton[this.size][this.size];
        this.winner = false;
        initializeMenuBar();
        initializeBoard();
        this.user = "x";
        this.ai = "o";
        this.currentPlayer = user;
    }

    JButton createButton()
    {
        JButton button = new JButton();
        button.setBackground(Color.WHITE);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 100));
        
        button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(((JButton) e.getSource()).getText().equals("") && winner == false)
                {
                    button.setText(user);
                    checkForWinner();
                    
                    if(winner == false)
                        getOpponentsMove();
                }
            } 
        });

        return button;
    }

    private void initializeBoard()
    {
        for(int i = 0; i < size; ++i)
        {
            for(int j = 0; j < size; ++j)
            {
                JButton button = createButton();
                board[i][j] = button;
                pane.add(button);
            }
        }
    }

    private void initializeMenuBar()
    {
        this.menuBar = new JMenuBar();
        this.menu = new JMenu("File");
        this.newGame = new JMenuItem("New Game");
        
        this.newGame.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
            }
        });

        this.menu.add(newGame);
        this.menuBar.add(this.menu);
        setJMenuBar(menuBar);
    }

    private void resetBoard()
    {
        this.winner = false;

        for(int i = 0; i < size; ++i)
        {
            for(int j = 0; j < size; ++j)
            {
                board[i][j].setText("");
            }
        }
    }

    private void checkForWinner()
    {
        for(int i = 0; i  < size; ++i)
        {
            // horizontal
            if( hasWon(this.board[i][0].getText(), this.board[i][1].getText(), this.board[i][2].getText()) )
            {
                this.winner = true;
                break;
            }
            // vertical
            else if( hasWon(this.board[0][i].getText(), this.board[1][i].getText(), this.board[2][i].getText()) )
            {
                this.winner = true;
                break;
            }
        }

        // diagonals
        if(this.winner == false)
        {
            if( hasWon(this.board[0][0].getText(), this.board[1][1].getText(), this.board[2][2].getText()) )
            {
                this.winner = true;
            }
            if( hasWon(this.board[0][2].getText(), this.board[1][1].getText(), this.board[2][0].getText()) )
            {
                this.winner = true;
            }
        }

        if(this.winner == true)
        {
            JOptionPane.showMessageDialog(null, "Player " + this.currentPlayer + " has won");
        }
        else if(isTie())
        {
            JOptionPane.showMessageDialog(null, "Tie");
        }
    }

    private void getOpponentsMove()
    {
        int bestMove[] = getBestMove();
        this.currentPlayer = ai;

        if(bestMove[0] != -1 && bestMove[1] != -1 && this.winner == false)
        {
            board[bestMove[0]][bestMove[1]].setText(ai);
            checkForWinner();
        }

        this.currentPlayer = user;
    }
    
    // get best move for ai
    // loop through all possible moves and get best
    private int[] getBestMove()
    {
        int bestScore = Integer.MIN_VALUE;
        int bestMove [] = {-1,-1};
        int score = Integer.MIN_VALUE;;

        for(int i = 0; i < size; ++i)
        {
            for(int j = 0; j < size; ++j)
            {
                if( board[i][j].getText().equals("") )
                {
                    board[i][j].setText(ai);

                    score = minimax(0, false);

                    board[i][j].setText("");

                    if(score > bestScore)
                    {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j; 
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMax)
    {
        int evaluation = evaluate();

        // return if no more moves can be made
        if(evaluation != -99)
            return evaluation;

        int bestScore, score;

        // if its maximizers turn
        if(isMax)
            bestScore = Integer.MIN_VALUE;
        else
            bestScore = Integer.MAX_VALUE;

        for(int i = 0; i < size; ++i)
        {
            for(int j = 0; j < size; ++j)
            {
                if( board[i][j].getText().equals("") )
                {
                    if(isMax)
                    {
                        board[i][j].setText(ai);
                        score = minimax(depth + 1, false);
                        bestScore = Math.max(bestScore, score);
                    }
                    else
                    {
                        board[i][j].setText(user);
                        score = minimax(depth + 1, true);
                        bestScore = Math.min(bestScore, score);
                    }

                    board[i][j].setText("");
                }
            }
        }

        return bestScore;
    }

    boolean hasWon(String a, String b, String c)
    {
        if( a.equals(b) && b.equals(c) && (a.equals("") == false))
            return true;
        
        return false;
    }

    boolean isTie()
    {
        boolean isTie = true;

        for(int i = 0; i < size; ++i)
        {
            for(int j = 0; j < size; ++j)
            {
                if(board[i][j].getText().equals(""))
                {
                    isTie = false;
                    break;
                }
            }

            if( !isTie )
                break;
        }

        return isTie;
    }

    /*
        1 - ai has won
        0 - tie
        -1 user has won
        -99 - more moves are still left to be made
    */
    private int evaluate()
    {
        // horizontal
        for(int i = 0; i < size; ++i)
        {
            if( hasWon(this.board[i][0].getText(), this.board[i][1].getText(), this.board[i][2].getText()) )
            {
                if(this.board[i][0].getText().equals(ai))
                    return 1;
                else
                    return -1;
            }
        }

        // vertical
        for(int i = 0; i < size; ++i)
        {
            if( hasWon(this.board[0][i].getText(), this.board[1][i].getText(), this.board[2][i].getText()) )
            {
                if(this.board[0][i].getText().equals(ai))
                    return 1;
                else
                    return -1;
            }
        }
        
        // diagonals
        if( hasWon(this.board[0][0].getText(), this.board[1][1].getText(), this.board[2][2].getText()) )
        {
            if(this.board[0][0].getText().equals(ai))
                return 1;
            else
                return -1;
        }

        if( hasWon(this.board[0][2].getText(), this.board[1][1].getText(), this.board[2][0].getText()) )
        {
            if(this.board[0][2].getText().equals(ai))
                return 1;
            else
                return -1;
        }

        if ( isTie() )
            return 0;

        return -99;
    }
}