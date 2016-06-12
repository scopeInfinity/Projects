#Merge Sort Implemention for MIPS
# scopeInfinity :P

# My Conventions
#	$s0 = N
#	$s1 = &A[0]
#	$s3 = &A[i]

.data
A:	.space 400			#100 Elements
B:	.space 400			#100 Elements
promptN:	.asciiz	"Enter Number of Elements : "
promptNum:	.asciiz	"Enter Numbers : "
sortMsg:	.asciiz	"Sorted Numbers : "
NoEle:		.asciiz	"No Element!"
Space:		.asciiz	" "


.text
main:
	li $v0, 4
	la $a0, promptN 
	syscall
	li $v0, 5
	syscall
	move $s0, $v0	#N
	ble $s0, $0, ExitNoElement 

	li $v0, 4
	la $a0, promptNum
	syscall

	move $t0, $s0	#Counter
	la $s3, A
	move $s1, $s3

loopInput:
		li $v0, 5
		syscall
		sw $v0, 0($s3)
		addi $s3, $s3, 4
		addi $t0, $t0, -1
		bne $t0, $0, loopInput

	sw $ra, 0($sp)
	addi $sp, $sp, 4
	jal mergeSortInit
	addi $sp, $sp, -4
	lw $ra, 0($sp)

	li $v0, 4
	la $a0, sortMsg 
	syscall

	move $t0, $s0	#Counter
	la $s3, A
loopOutput:
		lw $a0, 0($s3)
		li $v0, 1
		syscall
		la $a0, Space
		li $v0, 4
		syscall
		addi $s3, $s3, 4
		addi $t0, $t0, -1
		bne $t0, $0, loopOutput
	
Exit:
	li $v0, 10	#Exit
	syscall

ExitNoElement:
	li $v0, 4
	la $a0, NoEle
	syscall
	j Exit

mergeSortInit:
	move $a0, $s1
	sll $t0, $s0, 2 
	add $a1, $a0, $t0
	addi $a1, $a1, -4
	sw $ra, 0($sp)
	addi $sp, $sp, 4
	jal mergeSort
	addi $sp, $sp, -4
	lw $ra, 0($sp)
	jr $ra
	

#Arguments F=$a0, L=$a1
mergeSort:
	bge $a0, $a1, mergeSortEnd		#if(F>L) return;

	sub $t1, $a1, $a0
	addi $t1, $t1, 4
	srl $t1, $t1, 2 		#t1 = n

	srl $t0, $t1, 1			#n/2
	sll $t0, $t0, 2
	add $t0, $t0, -4
	add $t0, $t0, $a0		#F+n/2-1

	sw $ra, 0($sp)			#Store Activation Record
	sw $a0, 4($sp)
	sw $a1, 8($sp)
	sw $t0, 12($sp)
	addi $sp, $sp, 16
	#Parameters
	move $a1, $t0
	jal mergeSort
	addi $sp, $sp, -16
	lw $ra, 0($sp)			#Load Activation Record
	lw $a0, 4($sp)
	lw $a1, 8($sp)
	lw $t0, 12($sp)

	addi $t0, $t0, 4		#M

	sw $ra, 0($sp)			#Store Activation Record
	sw $a0, 4($sp)
	sw $a1, 8($sp)
	sw $t0, 12($sp)
	addi $sp, $sp, 16
	#Parameters
	move $a0, $t0
	jal mergeSort
	addi $sp, $sp, -16
	lw $ra, 0($sp)			#Load Activation Record
	lw $a0, 4($sp)
	lw $a1, 8($sp)
	lw $t0, 12($sp)
	
	addi $t0, $t0, -4		#M

	sw $ra, 0($sp)			#Store Activation Record
	addi $sp, $sp, 4
	#Parameters
	move $a2, $a1
	move $a1, $t0
	jal merge
	addi $sp, $sp, -4
	lw $ra, 0($sp)			#Load Activation Record
	

	mergeSortEnd:
	jr $ra

#Arguments $a0, $a1, $a2
merge:
	move $t5, $a0			#i
	move $t6, $a1			
	addi $t6, $t6, 4		#j
	la $t7, B				#k

loop1:
	sle $t3, $t5, $a1
	sle $t4, $t6, $a2
	and $t3, $t3, $t4
	beq $t3, $0, loop1End

	lw $t0, 0($t5)
	lw $t1, 0($t6)
	blt $t0, $t1, merge_if1
	sw $t1, 0($t7)
	add $t6, $t6, 4
	j merge_if1_e
	merge_if1:
	sw $t0, 0($t7)
	add $t5, $t5, 4
	merge_if1_e:
	add $t7, $t7, 4
	j loop1
loop1End:

loop2:
	sle $t4, $t6, $a2
	beq $t4, $0, loop2End

	lw $t1, 0($t6)
	sw $t1, 0($t7)
	add $t6, $t6, 4
	add $t7, $t7, 4

	j loop2
loop2End:

loop3:
	sle $t4, $t5, $a1
	beq $t4, $0, loop3End

	lw $t1, 0($t5)
	sw $t1, 0($t7)
	add $t5, $t5, 4
	add $t7, $t7, 4

	j loop3
loop3End:

	move $t5, $a0			#i
	la $t7, B				#j

loop4:
	sle $t4, $t5, $a2
	beq $t4, $0, loop4End

	lw $t1, 0($t7)
	sw $t1, 0($t5)
	add $t5, $t5, 4
	add $t7, $t7, 4

	j loop4
loop4End:


	jr $ra
