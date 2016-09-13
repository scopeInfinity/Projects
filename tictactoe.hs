import System.IO
import System.Process

getR x = 2-(quot (x-1) 3)
getC x = mod (x-1) 3 

--Print Board
printBoardRow board row
    | row<3 = do
                 putStrLn("| "++[(board !! row !! 0)]++" | "++[(board !! row !! 1)]++" | "++[(board !! row !! 2)]++" |") 
                 putStrLn("|---|---|---|")
                 printBoardRow board (row+1)
                 return ()
    | otherwise = do
                 putStrLn("    ")
                 return()


printBoard board = do
    putStrLn("|---|---|---|")
    printBoardRow board 0
    return ()

--https://rosettacode.org/wiki/Assigning_Values_to_an_Array
setIndex xs ii v
    | ii < 0 = error "Bad index"
    | otherwise = _setIndex xs ii v
    where
        _setIndex [] _ _ = error "Bad index"
        _setIndex (_ : xs) 0 v = v : xs
        _setIndex (x : xs) ii v = x : (setIndex xs (ii - 1) v)
checkRow board r
    | (r<3 && (board !! r !! 0)==(board !! r !! 1) && (board !! r !! 1)==(board !! r !! 2) && (board !! r !! 0)/=' ') = do{putStrLn("Winner is "++[(board !! r !! 0)]);return ()}
    | (r<3) = do
        checkRow board (r+1)
        return ()      
    | otherwise = do
        return ()

checkCol board c
    | (c<3 && (board !! 0 !! c)==(board !! 1 !! c) && (board !! 1 !! c)==(board !! 2 !! c) && (board !! 0 !! c)/=' ') = do{putStrLn("Winner is "++[(board !! 0 !! c)]);return ()}
    | (c<3) = do
        checkCol board (c+1)
        return ()      
    | otherwise = do
        return ()

checkDig board
    | ((board !! 0 !! 0)==(board !! 1 !! 1) && (board !! 1 !! 1)==(board !! 2 !! 2) && (board !! 0 !! 0)/=' ') = do{putStrLn("Winner is "++[(board !! 0 !! 0)]);return ()}
    | ((board !! 2 !! 0)==(board !! 1 !! 1) && (board !! 1 !! 1)==(board !! 0 !! 2) && (board !! 2 !! 0)/=' ') = do{putStrLn("Winner is "++[(board !! 2 !! 0)]);return ()}
    | otherwise = do
        return ()

checkNotFullRow board r
    | (r<3 && ((board !! r !! 0)==' ' || (board !! r !! 1)==' ' || (board !! r !! 2)==' ')) = True
    | otherwise = False

checkFull board
    | (checkNotFullRow board 0) = return()
    | (checkNotFullRow board 1) = return()
    | (checkNotFullRow board 2) = return()
    | otherwise = do
        putStrLn("Match Draw")
        return ()

checkwin board = do
    checkRow board 0
    checkCol board 0
    checkDig board
    checkFull board
    return ()
    

changeTurn turn
    | turn == 'X' = 'O'
    | otherwise = 'X'

moves board turn input 
    | ((board !! (getR input) !! (getC input)) == ' ') = do
        state (setIndex board (getR input) (setIndex (board !! (getR input)) (getC input) turn)) (changeTurn(turn))
        return()
    | otherwise
     = do
        putStrLn("Invalid Move! Try Again!")
        state board turn
        return ()

state board turn = do
    callCommand "clear"
    putStrLn("Cross and Circle\n==================\n\n")
    printBoard board
    checkwin board
    putStrLn("Turn : "++[turn])
    input  <- getLine
    moves board turn (read input :: Int)
    return ()    

main = do
    state ([[' ',' ',' '],[' ',' ',' '],[' ',' ',' ']]) 'X'
    return ()  


