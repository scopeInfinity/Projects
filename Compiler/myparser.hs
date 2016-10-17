{-# LANGUAGE DeriveDataTypeable #-}

import Text.Regex.Posix
import Data.Typeable
import Data.Data
import Data.String.Utils as Utils

-- Basic Functions

--Custom split Function, for blank space
isBlankSpace :: Char -> Bool
isBlankSpace ch
  | ch == ' ' = True
  | ch == '\n' = True
  | ch == '\t' = True
  | otherwise = False

splitthis :: String -> [String]
splitthis "" = []
splitthis str = (firstWord str):(splitthis (nextWords str))

firstWord :: String -> String
firstWord "" = ""
firstWord str
  | isBlankSpace $ head str = ""
  | otherwise  = (head str):(firstWord (tail str))

nextWords :: String -> String
nextWords "" = ""
nextWords str
  | isBlankSpace $ head str = skipAllSpaces (tail str)
  | otherwise  = (nextWords (tail str))

skipAllSpaces :: String -> String
skipAllSpaces "" = ""
skipAllSpaces str
  | isBlankSpace $ head str = skipAllSpaces (tail str)
  | otherwise = str

--Allowed Char

data Token =
  Invalid {str :: String}
  | DInt {val :: Integer}
  | DChar {valc :: Char}
  | Dopa {op :: String}
  | Dopb {op :: String}
  | Dopr {op :: String}
  | DReserved {word :: String}
  | DType {datatype :: String}
  | DIdentifier {name :: String} 
  deriving (Show,Eq,Typeable,Data)
data Tokens = Tokens {list :: [Token]} deriving (Show)

sameType x y = (toConstr x) ==  (toConstr y)
diffType x y = (toConstr x) /=  (toConstr y)

isValidToken :: Token -> Bool
isValidToken x = diffType x (Invalid "")

isInvalidToken :: Token -> Bool
isInvalidToken x = sameType x (Invalid "")

checkReserved :: String -> Token
checkReserved input 
  | input =~ "^((:=)|(skip)|;|(if)|(then)|(else)|(while))$" :: Bool = DReserved input
  | otherwise = Invalid input

checkDataType :: String -> Token
checkDataType input 
  | input =~ "^((char)|(int))$" :: Bool = DType input
  | otherwise = Invalid input

checkInt :: String -> Token
checkInt input 
  | input =~ "^[0-9]+$" :: Bool = DInt (read input :: Integer)
  | otherwise = Invalid input

checkChar :: String -> Token
checkChar input 
  | input =~ "^'.'$" :: Bool = DChar (input !! 1)
  | otherwise = Invalid input

checkOperator :: String -> Token
checkOperator input 
  | input =~ "^((\\+)|(\\-)|(\\*)|(\\/))$" :: Bool = Dopa input
  | input =~ "^((and)|(or)|(not))$" :: Bool = Dopb input
  | input =~ "^(([<>]=?)|(==)|(!=))$" :: Bool = Dopr input
  | otherwise = Invalid input

checkIdentifier :: String -> Token
checkIdentifier input 
  | input =~ "^[A-Za-z_][A-Za-z_0-9]*$" :: Bool = DIdentifier input
  | otherwise = Invalid input

getToken :: String -> Token
getToken x 
  | isValidToken (checkReserved x) = checkReserved x
  | isValidToken (checkOperator x) = checkOperator x
  | isValidToken (checkDataType x) = checkDataType x
  | isValidToken (checkInt x) = checkInt x
  | isValidToken (checkChar x) = checkChar x
  | isValidToken (checkIdentifier x) = checkIdentifier x
  | otherwise = Invalid x


makeExpression :: String -> Tokens
makeExpression input = Tokens (map getToken (splitthis input))

getTokenList :: Tokens -> [Token]
getTokenList (Tokens list) = list

getInvalidTokens :: Tokens -> Tokens
getInvalidTokens (Tokens []) = Tokens ([] :: [Token]) 
getInvalidTokens (Tokens list) 
  | isInvalidToken (head list) = Tokens $ (head list ):getTokenList(getInvalidTokens $ Tokens $ tail list)
  | otherwise = getInvalidTokens $ Tokens $ tail list

nop :: IO ()
nop = sequence_ []

printTokens [] = return 0
printTokens tokens = do
  putStr ((show(head tokens)) ++ "\n")
  printTokens (tail tokens)
  error "Invalid Token Found!!"
  return 0;

-- In bytes
getSize :: Token -> Integer
getSize x
  | x == checkDataType("int") = 4
  | x == checkDataType("char") = 1
  | otherwise = error "Invalid DataType"

-- Symbol Table
data SymbolRow = 
  SymbolRow {ident :: Token, itype :: Token, size :: Integer}

instance Show SymbolRow where
  show (SymbolRow (DIdentifier ident) (DType itype) size) = "{ "++ident ++", "++ itype ++", "++ show(size)++"}\n"


cleanInput :: String -> String
cleanInput str = Utils.replace ";" " ; " str

-- DataType, rest of Token, out is SymbolTable
makeSymbolTable :: Token -> [Token] -> SymbolRow
makeSymbolTable token tokens
  | (sameType (head tokens) (DIdentifier "x")) && ((tokens !! 1) == checkReserved ";" ) = SymbolRow (head tokens) (token) (getSize token)
  | otherwise = error "Error! On Variable Declaration"

getSymbolTable :: [Token] -> [SymbolRow]
getSymbolTable [] = []
getSymbolTable tokens
  | sameType (head tokens) (DType " ") = (makeSymbolTable (head tokens) (tail tokens)) : (getSymbolTable (tail tokens))
  | otherwise = getSymbolTable (tail tokens)

main :: IO ()
main = do
  putStr "Enter filename\n"
  filename <- getLine
  a <- readFile filename
  let tokens_collection = (makeExpression $ cleanInput a)
  let invalid_tokens = getInvalidTokens tokens_collection
  printTokens (getTokenList invalid_tokens)
  putStr $ (show(tokens_collection) ++ "\n")
  putStr $ "\nSymbol Table\n===========================\n\n"
  putStr $ show $ getSymbolTable $ getTokenList tokens_collection
  putStr $ "\n===========================\n\n"
