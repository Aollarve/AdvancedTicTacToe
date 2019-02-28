import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class AdvancedTicTacToe {
	
	static int Bplay = -1; // Board where computer is playing
	static int Cplay = -1; // Position in board that Computer is playing
	static char Clet = 'O';
	static int GameOver = 0;
	static int boardToPlay = 0;
	static boolean inplay = false;
	static boolean insideInplay = false;
	
	static void computerPlay(char[][] board, int b) {
		Hminimax(board, b, Clet, 7, Integer.MIN_VALUE, Integer.MAX_VALUE);
		System.err.print("Computer Board Play: ");
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println((Bplay+1) + " " + (Cplay+1));
		
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		board[Bplay][Cplay] = Clet;
		
	}
	
	static boolean empty(char[][] board, int b) {
		if(board[b][0] == '-' && board[b][1] == '-' && board[b][2] == '-' && board[b][3] == '-'
				&& board[b][4] == '-' && board[b][5] == '-' && board[b][6] == '-' && board[b][7] == '-'
				&& board[b][8] == '-') {
			return true;
		} else {
			return false;
		}
	}

	
	static int Hminimax(char[][] board, int b, char p, int depth, int alpha, int beta) { // Minimax with Alpha-Beta Pruning
		int cBoardOver = boardOver(board, b); // BoardOver placed in variable as to not call the function in if loops
		int almostBoardOver = boardAlmostOver(board, b, p); // Placed in variable as to not call the function in if loops
		if((almostBoardOver != -1) && Clet == p) {
			Bplay = b;
			Cplay = almostBoardOver;
			return 100;
		} else if(((almostBoardOver != -1) && Clet != p)) {
			return -100;
		} 
		
		else if(depth == 0) {
			return evaluate(board, b, p);
		}
		
		 
		
		int[][] score = new int[9][9];
		//int[][] move = new int[9][9];
		
		char op = 'X';
		if(p == 'X') {
			op = 'O';
		}
		
		if(cBoardOver == -2 || (!inplay && !insideInplay)) {
			insideInplay = true;
			if(p == Clet) {
				//int max = Integer.MIN_VALUE;
				int bplay = -1;
				int cplay = -1;
				boolean br = false;
				for(int i = 0; i < 9; i++) {
					for(int e = 0; e < 9; e++) {
						if(board[i][e] == '-') {
							char[][] boardCopy = new char[9][9];
							boardCopy = deepCopy(board);
							boardCopy[i][e] = p;
							score[i][e] = Hminimax(boardCopy, e, op, depth-1, alpha, beta);
							//move[i][e] = 1;
							if(score[i][e] > alpha) {
								alpha = score[i][e];
								if(alpha >= beta) {
									br = true;
									break;
								}
								bplay = i;
								cplay = e;
							}
						}
					}
					if(br) {
						break;
					}
				}
				
				Bplay = bplay;
				Cplay = cplay;
				return alpha;
				
			} else {
				int min = Integer.MAX_VALUE;
				boolean br = false;
				for(int i = 0; i < 9; i++) {
					for(int e = 0; e < 9; e++) {
						if(board[i][e] == '-') {
							char[][] boardCopy = new char[9][9];
							boardCopy = deepCopy(board);
							boardCopy[i][e] = p;
							score[i][e] = Hminimax(boardCopy, e, op, depth-1, alpha, beta);
							if(score[i][e] < beta) {
								if(alpha >= beta) {
									br = true;
									break;
								}
								beta = score[i][e];
							}
						}
					}
					if(br) {
						break;
					}
				}
				
				return beta ;
				
			}
			 
			
		} else {
				
				if(p == Clet) {
					int bplay = -1;
					int cplay = -1;
					boolean br = false;
					for(int e = 0; e < 9; e++) {
						if(board[b][e] == '-') {
							char[][] boardCopy = new char[9][9];
							boardCopy = deepCopy(board);
							boardCopy[b][e] = p;
							score[b][e] = Hminimax(boardCopy, e, op, depth-1, alpha, beta);
							
							if(score[b][e] > alpha) {
								alpha = score[b][e];
								if(alpha >= beta) {
									br = true;
									break;
								}
								bplay = b;
								cplay = e;
							}
						}
					}
					
					Bplay = bplay;
					Cplay = cplay;
					return alpha;
					
				} else {
					for(int e = 0; e < 9; e++) {
						if(board[b][e] == '-') {
							char[][] boardCopy = new char[9][9];
							boardCopy = deepCopy(board);
							boardCopy[b][e] = p;
							score[b][e] = Hminimax(boardCopy, e, op, depth-1, alpha, beta);
							if(score[b][e] < beta) {
								if(alpha >= beta) {
									break;
								}
								beta = score[b][e];
							}
						}
					}
					
					return beta;
					
				}
		}
	}

	
	
	static int evaluate(char[][] board, int b, char p) {
		
		int numX = 0; // Number of boards almost over by X
		int numO = 0; // Number of boards almost over by O
		int[] boardAlmostX = new int[9]; // board almost over by X
		int[] boardAlmostO = new int[9]; // board almost over by O
		
		for(int i = 0; i < 9; i++) {
			if(boardAlmostOver(board, i, 'X') != -1) {
				numX++;
				boardAlmostX[i] = 1;
			} 
			if(boardAlmostOver(board, i, 'O') != -1) {
				numO++;
				boardAlmostO[i] = 1;
			}
		}
		
		int possiblePlays = 9;
		for(int i = 0; i < 9; i++) {
			if(p == 'X') {
				if(boardAlmostO[i] == 1 && board[b][i] == '-') {
					possiblePlays--;
				} else if(board[b][i] != '-') {
					possiblePlays--;
				}
			} else {
				if(boardAlmostX[i] == 1 && board[b][i] == '-') {
					possiblePlays--;
				} else if(board[b][i] != '-') {
					possiblePlays--;
				}
			}
		}
		
		if(possiblePlays == 0 && Clet == p) {
			return -100;
		} else if(possiblePlays == 0 && Clet != p) {
			return 100;
		} else if(Clet == 'X') {
			return (numX*10)-(numO*10);
		} else {
			return (numO*10)-(numX*10);
		}
		
	}
	
	static int boardAlmostOver(char[][] board, int b, char  p) { // the board b is almost won by the player p (in char for either X or O)
		
		if((board[b][1]== p && board[b][1] == board[b][2] && board[b][0] == '-')
				||(board[b][3]== p && board[b][3] == board[b][6] && board[b][0] == '-')
				||(board[b][4]== p && board[b][4] == board[b][8] && board[b][0] == '-')) {
			return 0;
		} else if((board[b][0]== p && board[b][0] == board[b][2] && board[b][1] == '-')
				||(board[b][4]== p && board[b][4] == board[b][7] && board[b][1] == '-')) {
			return 1;
		} else if((board[b][0]== p && board[b][0] == board[b][1] && board[b][2] == '-')
				||(board[b][4]== p && board[b][4] == board[b][6] && board[b][2] == '-')
				||(board[b][5]== p && board[b][5] == board[b][8] && board[b][2] == '-')) {
			return 2;
		} else if((board[b][0]== p && board[b][0] == board[b][6] && board[b][3] == '-')
				||(board[b][4]== p && board[b][4] == board[b][5] && board[b][3] == '-')) {
			return 3;
		} else if((board[b][0]== p && board[b][0] == board[b][8] && board[b][4] == '-')
				||(board[b][2]== p && board[b][2] == board[b][6] && board[b][4] == '-')
				||(board[b][3]== p && board[b][3] == board[b][5] && board[b][4] == '-')) {
			return 4;
		} else if((board[b][2]== p && board[b][2] == board[b][8] && board[b][5] == '-')
				||(board[b][3]== p && board[b][3] == board[b][4] && board[b][5] == '-')) {
			return 5;
		} else if((board[b][0]== p && board[b][0] == board[b][3] && board[b][6] == '-')
				||(board[b][2]== p && board[b][2] == board[b][4] && board[b][6] == '-')
				||(board[b][7]== p && board[b][7] == board[b][8] && board[b][6] == '-')) {
			return 6;
		} else if((board[b][1]== p && board[b][1] == board[b][4] && board[b][7] == '-')
				||(board[b][6]== p && board[b][6] == board[b][8] && board[b][7] == '-')) {
			return 7;
		} else if((board[b][0]== p && board[b][0] == board[b][4] && board[b][8] == '-')
				||(board[b][2]== p && board[b][2] == board[b][5] && board[b][8] == '-')
				||(board[b][6]== p && board[b][6] == board[b][7] && board[b][8] == '-')) {
			return 8;
		} else {
			return -1;
		}
	}
	
	static int boardOver(char[][] board, int b) {
		if((board[b][0] == 'X' && board[b][0] == board[b][1] && board[b][1] == board[b][2])
				||(board[b][3] == 'X' && board[b][3] == board[b][4] && board[b][4] == board[b][5])
				||(board[b][6] == 'X' && board[b][6] == board[b][7] && board[b][7] == board[b][8])
				||(board[b][0] == 'X' && board[b][0] == board[b][3] && board[b][3] == board[b][6])
				||(board[b][1] == 'X' && board[b][1] == board[b][4] && board[b][4] == board[b][7])
				||(board[b][2] == 'X' && board[b][2] == board[b][5] && board[b][5] == board[b][8])
				||(board[b][0] == 'X' && board[b][0] == board[b][4] && board[b][4] == board[b][8])
				||(board[b][2] == 'X' && board[b][2] == board[b][4] && board[b][4] == board[b][6])) {
			return 1; // X won board
		} else if((board[b][0] == 'O' && board[b][0] == board[b][1] && board[b][1] == board[b][2])
				||(board[b][3] == 'O' && board[b][3] == board[b][4] && board[b][4] == board[b][5])
				||(board[b][6] == 'O' && board[b][6] == board[b][7] && board[b][7] == board[b][8])
				||(board[b][0] == 'O' && board[b][0] == board[b][3] && board[b][3] == board[b][6])
				||(board[b][1] == 'O' && board[b][1] == board[b][4] && board[b][4] == board[b][7])
				||(board[b][2] == 'O' && board[b][2] == board[b][5] && board[b][5] == board[b][8])
				||(board[b][0] == 'O' && board[b][0] == board[b][4] && board[b][4] == board[b][8])
				||(board[b][2] == 'O' && board[b][2] == board[b][4] && board[b][4] == board[b][6])) {
			return -1; // O won board
		} else if(board[b][0] != '-' && board[b][1] != '-' && board[b][2] != '-' && board[b][3] != '-'
					&& board[b][4] != '-' && board[b][5] != '-' && board[b][6] != '-' && board[b][7] != '-'
					&& board[b][8] != '-') {
			return -2; // Board Full
		} else {
			return 0;
		}
		
		
	}
	
	static int gameOver(char[][] board) {
		if(boardOver(board, 0)==1||boardOver(board, 1)==1||boardOver(board, 2)==1
				||boardOver(board, 3)==1||boardOver(board, 4)==1||boardOver(board, 5)==1
				||boardOver(board, 6)==1||boardOver(board, 7)==1||boardOver(board, 8)==1) {
			return 1; // X won game
		} else if(boardOver(board, 0)==-1||boardOver(board, 1)==-1||boardOver(board, 2)==-1
				||boardOver(board, 3)==-1||boardOver(board, 4)==-1||boardOver(board, 5)==-1
				||boardOver(board, 6)==-1||boardOver(board, 7)==-1||boardOver(board, 8)==-1) {
			return -1; // O won game
		} else if(boardOver(board, 0)==-2&&boardOver(board, 1)==-2&&boardOver(board, 2)==-2
				&&boardOver(board, 3)==-2&&boardOver(board, 4)==-2&&boardOver(board, 5)==-2
				&&boardOver(board, 6)==-2&&boardOver(board, 7)==-2&&boardOver(board, 8)==-2) {
			return -2; // Tie
		} else {
			return 0; // Game not over
		}
	}
	
	public static char[][] deepCopy(char[][] array) {  // This was just a basic way to do deepcopy that I was able to duplicate for this one
	    if (array == null) {
	        return null;
	    }

	    final char[][] result = new char[array.length][];
	    for (int i = 0; i < array.length; i++) {
	        result[i] = Arrays.copyOf(array[i], array[i].length);
	    }
	    return result;
	}

	public static void main(String[] args) {
		char[][] board = new char[9][9]; // board to play advanced TicTacToe
		for (char[] row: board)
			Arrays.fill(row, '-');
		Scanner sc = new Scanner(System.in);
		
		int exit = 0;
		char pbyp = ' '; // stores the letter the player is playing
		
		while(exit != 1) {
			// THIS WAS ALTERED IN ORDER TO PLAY IN TOURNAMENT
			System.err.print("What is the program playing? ( X or O, E to exit) "); 
			pbyp = sc.next().charAt(0);
			pbyp = Character.toUpperCase(pbyp);
			while(pbyp != 'X' && pbyp != 'O' && pbyp != 'E') {
				System.err.print("Incorrect Input. Would you like to play X or O? (E to exit) ");
				pbyp = sc.next().charAt(0);
				pbyp = Character.toUpperCase(pbyp);
			}
			
			// INVERTED IN ORDER TO PLAY IN TOURNAMENT
			if(pbyp == 'X') {
				Clet = 'X';
				pbyp = 'O';
			} else if(pbyp == 'O') {
				Clet = 'O';
				pbyp = 'X';
			} else {
				break;
			}

			while(gameOver(board) == 0) {
				if(Clet == 'X') {
					computerPlay(board, boardToPlay);
					boardToPlay = Cplay;
					inplay = true;
					System.err.println();
					for(int i=0; i < 9; i++) {
						
						if(i%3 == 0) {
							System.err.println();
						}
						for(int j=0; j < 3; j++) {
							for(int e=0; e < 3; e++) {
								System.err.print(board[j+(i/3)*3][e+(i%3)*3]);
							}
							System.err.print("  ");
						}
						System.err.println();
					}
					System.err.println();
					if (gameOver(board) != 0) {
						break;
					}
				}
				
				
				
				// BOARD
				System.err.print("Board? (1-9): ");
				int bplay = 0;
				if(sc.hasNextInt()) {
					bplay = sc.nextInt();
				} else {
					sc.next();
				}
				
				while(bplay < 1 || bplay > 9 || (inplay && (bplay-1 != boardToPlay) && (boardOver(board, boardToPlay) != -2))
						|| (boardOver(board, boardToPlay) == -2)) {
					System.err.print("Invalid Move. Board? (1-9): ");
					if(sc.hasNextInt()) {
						bplay = sc.nextInt();
					} else {
						sc.next();
					}
				}

				
				
				// POSITION IN BOARD
				System.err.print("Position in board " + bplay + "? (1-9): ");
				int play = 0;
				if(sc.hasNextInt()) {
					play = sc.nextInt();
				} else {
					sc.next();
				}
				while(play < 1 || play > 9 || board[bplay-1][play-1] != '-') {
					System.err.print("Invalid Move. Position in board? (1-9): ");
					if(sc.hasNextInt()) {
						play = sc.nextInt();
					} else {
						sc.next();
					}
				}
				
				// PLAY
				board[bplay-1][play-1] = pbyp;
				boardToPlay = play-1;
				inplay = true;
				
				for(int i=0; i < 9; i++) {
					
					if(i%3 == 0) {
						System.err.println();
					}
					for(int j=0; j < 3; j++) {
						for(int e=0; e < 3; e++) {
							System.err.print(board[j+(i/3)*3][e+(i%3)*3]);
						}
						System.err.print("  ");
					}
					System.err.println();
				}
				
				if (gameOver(board) != 0) {
					break;
				}
	
				if(Clet == 'O') {
					computerPlay(board, boardToPlay);
					boardToPlay = Cplay;
					inplay = true;
					System.err.println();
					for(int i=0; i < 9; i++) {
						
						if(i%3 == 0) {
							System.err.println();
						}
						for(int j=0; j < 3; j++) {
							for(int e=0; e < 3; e++) {
								System.err.print(board[j+(i/3)*3][e+(i%3)*3]);
							}
							System.err.print("  ");
							
						}
						System.err.println();
					}
					System.err.println();
					if (gameOver(board) != 0) {
						break;
					}
				}
			}
			
			inplay = false;
			
			if((gameOver(board) == 1 && Clet == 'X') || (gameOver(board) == -1 && Clet == 'O')) {
				System.err.println("The Computer just won!");
			} else if(gameOver(board) == 0) {
				System.err.println("There was a tie!");
			} else  {
				System.err.println("You won against the computer!");
			}
			System.err.println();
			for (char[] row: board)
				Arrays.fill(row, '-');
		
		}
		
		
	}

}
