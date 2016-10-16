{-# LANGUAGE DeriveDataTypeable #-}

import Text.Regex.Posix
import Data.Typeable
import Data.Data

-- Basic Functions

--Custom Split Function, for blank space
split :: String -> [String]
split "" = []
split str = (firstWord str):(split (nextWords str))

firstWord :: String -> String
firstWord "" = ""
firstWord str
  | head str == ' ' = ""
  | otherwise  = (head str):(firstWord (tail str))

nextWords :: String -> String
nextWords "" = ""
nextWords str
  | head str == ' ' = skipAllSpaces (tail str)
  | otherwise  = (nextWords (tail str))

skipAllSpaces :: String -> String
skipAllSpaces "" = ""
skipAllSpaces str
  | (head str) == ' ' = skipAllSpaces (tail str)
  | otherwise = str

--Allowed Char

data Token =
  Invalid {str :: String}
  | DInt {val :: Integer}
  | DIdentifier {name :: String} 
  deriving (Show,Eq,Typeable,Data)
data Tokens = Tokens {list :: [Token]} deriving (Show)
--newtype Parser tokenType = Parser { parse :: String -> (Bool,[(tokenType,String)]) }

sameType x y = (toConstr x) ==  (toConstr y)
diffType x y = (toConstr x) /=  (toConstr y)

isValidToken :: Token -> Bool
isValidToken x = diffType x (Invalid "")

isInvalidToken :: Token -> Bool
isInvalidToken x = sameType x (Invalid "")

--Parser DInt
checkInt :: String -> Token
checkInt input 
  | input =~ "^[0-9]+$" :: Bool = DInt (read input :: Integer)
  | otherwise = Invalid input

checkIdentifier :: String -> Token
checkIdentifier input 
  | input =~ "^[A-Za-z_][A-Za-z_0-9]*$" :: Bool = DIdentifier input
  | otherwise = Invalid input

getToken :: String -> Token
getToken x 
  | isValidToken (checkInt x) = checkInt x
  | isValidToken (checkIdentifier x) = checkIdentifier x
  | otherwise = Invalid x

makeExpression :: String -> Tokens
makeExpression input = Tokens (map getToken (split input))

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

main :: IO ()
main = do
  putStr "Enter an expression\n"
  a <- getLine
  let tokens_collection = (makeExpression a)
  let invalid_tokens = getInvalidTokens tokens_collection
  printTokens (getTokenList invalid_tokens)
  putStr $ (show(tokens_collection) ++ "\n")