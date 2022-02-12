/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiedonpakkaus.domain;

/**
 * Huffmanin puumallin solmu.
 * 
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    
    char c;
    int frequency;
    HuffmanNode left; 
    HuffmanNode right;
    
    public HuffmanNode(char c, int frequency, HuffmanNode left, HuffmanNode right) {
        this.c = c;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
    
    public HuffmanNode(char c, int frequency) {
        this.c = c;
        this.frequency = frequency;
    }
    
    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public int compareTo(HuffmanNode otherNode) {
        return frequency - otherNode.frequency;
    }
}
