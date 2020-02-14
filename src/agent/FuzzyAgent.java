package agent;
import fuzzy.AntecedentThesis;
import fuzzy.InputValue;
import fuzzy.Rule;
import fuzzy.fuzzyinterface.FuzzyInterface1;
import fuzzy.relationmodel.RelationModelFactory;
import fuzzy.set.FuzzySet;
import fuzzy.set.TrapezoidFuzzySet;
import fuzzy.set.TriangleFuzzySet;
import rocketenv.Rocket;
import rocketenv.RocketListener;
import rocketenv.Space;

import java.util.ArrayList;
import java.util.List;

public class FuzzyAgent extends Agent implements RocketListener {
    private FuzzyInterface1 fuzzyRules;
    private InputValue error;
    private FuzzySet.Range errorRange;

    public FuzzyAgent(Rocket mRocket, Space mSpace) {
        super(mRocket, mSpace);

        RelationModelFactory relation=new RelationModelFactory(RelationModelFactory.MAMDANI_MODEL);

        errorRange=new FuzzySet.Range(-5,5);
        error =new InputValue("エラー",0);
        //前件部のファジィ集合「エラーが・・・」
        FuzzySet isMuchPositive=new TrapezoidFuzzySet(errorRange,"正方向にとても大きい",2,3,4,5);
        FuzzySet isWellPositive=new TriangleFuzzySet(errorRange,"正方向にやや大きい",1,2.5,4);
        FuzzySet isLittlePositive=new TriangleFuzzySet(errorRange,"正方向に少しだけ",0,0.7,1.4);
        FuzzySet isLittleLittlePositive=new TriangleFuzzySet(errorRange,"正方向にほんの少しだけ",0,0,0.1);
        FuzzySet isLittlelittleNegative=new TriangleFuzzySet(errorRange,"負方向にほんの少しだけ",-0.1,0,0);
        FuzzySet isLittleNegative=new TriangleFuzzySet(errorRange,"負方向に少しだけ",-1.4,-0.7,0);
        FuzzySet isWellNegative=new TriangleFuzzySet(errorRange,"負方向にやや大きい",-4,-2.5,-1);
        FuzzySet isMuchNegative=new TrapezoidFuzzySet(errorRange,"負方向にとても大きい",-5,-4,-3,-2);

        FuzzySet.Range mvRange=new FuzzySet.Range(-5,5);
        List<Double> mvValues=new ArrayList<>();
        for(double mv=mvRange.min;mv<=mvRange.max;mv+=0.1)mvValues.add(mv);
        //後件部のファジィ集合
        FuzzySet downFast=new TriangleFuzzySet(mvRange,"急いで降下する",-5,-5,-3.5);
        FuzzySet down=new TriangleFuzzySet(mvRange,"降下する",-4,-2,-1);
        FuzzySet downALittle=new TriangleFuzzySet(mvRange,"ゆっくり降下する",-1,-0.5,0);
        FuzzySet downALittleLittle=new TriangleFuzzySet(mvRange,"ほんの少し降下する",-0.1,0,0);
        FuzzySet upALittleLittle=new TriangleFuzzySet(mvRange,"ほんの少し上昇する",0,0,0.1);
        FuzzySet upALittle=new TriangleFuzzySet(mvRange,"ゆっくり上昇する",0,0.5,1);
        FuzzySet up=new TriangleFuzzySet(mvRange,"上昇する",1,2,4);
        FuzzySet upFast=new TriangleFuzzySet(mvRange,"急いで上昇する",3.5,5,5);

        fuzzyRules=new FuzzyInterface1(mvValues,relation.getPreferredCombinationModeBetweenRules());
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isMuchPositive),upFast,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isWellPositive),up,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isLittlePositive),upALittle,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isLittleLittlePositive),upALittleLittle,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isLittlelittleNegative),downALittleLittle,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isLittleNegative),downALittle,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isWellNegative),down,relation.create()));
        fuzzyRules.addRule(new Rule(new AntecedentThesis(error,isMuchNegative),downFast,relation.create()));

        System.out.println(fuzzyRules.toString());

        mRocket.addCarListener(this);
        onRocketRan(0.1);
    }


    @Override
    public void onRocketRan(double deltaTime) {
        double rowError=mSpace.getSV()-mRocket.getPV();

        //エラー値はerrorの定義域(errorRange)に入っていないといけない←
        if(rowError<errorRange.min)rowError=errorRange.min;
        if(errorRange.max<rowError)rowError=errorRange.max;
        error.setValue(rowError);

        try {
            fuzzyRules.inputValueChanged();
        } catch (Rule.NoAntecedentPartListException e) {
            e.printStackTrace();
        }
        try {
            mRocket.setMV(fuzzyRules.getConsequent());
        } catch (FuzzyInterface1.NoRuleException e) {
            e.printStackTrace();
        } catch (Rule.NoAntecedentPartListException e) {
            e.printStackTrace();
        }
    }
}
